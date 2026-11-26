package GymTrack.example.demo.controller;
import GymTrack.example.demo.model.*;
import GymTrack.example.demo.Service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/socios")
@RequiredArgsConstructor
public class SocioController {
    private final SocioService socioService;
    private final RolService rolService;
    private final RecepcionistaService recepcionistaService;

    @GetMapping
    public String findAll(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        cargarLista(model);
        return "socios";
    }

    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        return formulario(new Socio(), model);
    }

    @GetMapping("/editar/{id}")
    public String findById(@PathVariable Integer id, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        Socio actual = socioService.findById(id).orElse(null);
        if (actual == null) return "redirect:/socios";
        return formulario(actual, model);
    }

    @PostMapping("/guardar")
    public String save(@RequestParam(required = false) Integer id, @ModelAttribute Socio dato, @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.post(session, token)) return "redirect:/socios";
        boolean nuevo = id == null;
        Socio actual = nuevo ? new Socio() : socioService.findById(id).orElse(null);
        if (actual == null) return "redirect:/socios";
        String error = copiar(actual, dato, nuevo, session);
        if (error != null) {
            model.addAttribute("error", error);
            return formulario(actual, model);
        }
        try {
            socioService.save(actual);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se pudo guardar. Revisa datos duplicados y campos obligatorios.");
            return formulario(actual, model);
        }
        return "redirect:/socios";
    }

    @PostMapping("/eliminar/{id}")
    public String deleteById(@PathVariable Integer id, @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.post(session, token)) return "redirect:/socios";
        Socio actual = socioService.findById(id).orElse(null);
        if (actual == null) return "redirect:/socios";
        try {
            socioService.deleteById(id);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            cargarLista(model);
            return "socios";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se puede eliminar porque tiene registros relacionados.");
            cargarLista(model);
            return "socios";
        }
        return "redirect:/socios";
    }

    private void cargarLista(Model model) {
        model.addAttribute("lista", socioService.findAll());
    }

    private String formulario(Socio dato, Model model) {
        model.addAttribute("dato", dato);
        return "socios-form";
    }

    private String copiar(Socio actual, Socio dato, boolean nuevo, HttpSession session) {
        if (dato.getNom_socio() == null || dato.getNom_socio().isBlank() || dato.getCorreo_socio() == null || dato.getCorreo_socio().isBlank()) return "Nombre y correo obligatorios.";
        if (nuevo && (dato.getContra_socio() == null || dato.getContra_socio().isBlank())) return "La contraseña es obligatoria.";
        if (dato.getContra_socio() != null && !dato.getContra_socio().isBlank()) actual.setContra_socio(dato.getContra_socio());
        if (!"ACTIVO".equals(dato.getEstatus()) && !"INACTIVO".equals(dato.getEstatus())) return "Estatus inválido.";
        if (nuevo) {
            Rol rol = rolService.findByNombre("SOCIO");
            Recepcionista recepcionista = recepcionistaService.findById(Acceso.id(session)).orElse(null);
            if (rol == null || recepcionista == null) return "No se encontró el rol o el personal responsable.";
            actual.setRol(rol);
            actual.setRecepcionista(recepcionista);
        }
        actual.setNom_socio(dato.getNom_socio());
        actual.setNum_calle_s(dato.getNum_calle_s());
        actual.setCol_s(dato.getCol_s());
        actual.setEdo_s(dato.getEdo_s());
        actual.setCod_post_s(dato.getCod_post_s());
        actual.setTel_socio(dato.getTel_socio());
        actual.setCorreo_socio(dato.getCorreo_socio());
        actual.setEstatus(dato.getEstatus());
        return null;
    }
}
