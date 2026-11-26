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
@RequestMapping("/recepcionistas")
@RequiredArgsConstructor
public class RecepcionistaController {
    private final RecepcionistaService recepcionistaService;
    private final RolService rolService;

    @GetMapping
    public String findAll(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        cargarLista(model);
        return "recepcionistas";
    }

    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        return formulario(new Recepcionista(), model);
    }

    @GetMapping("/editar/{id}")
    public String findById(@PathVariable Integer id, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        Recepcionista actual = recepcionistaService.findById(id).orElse(null);
        if (actual == null || !"RECEPCIONISTA".equals(actual.getRol().getNombre())) return "redirect:/recepcionistas";
        return formulario(actual, model);
    }

    @PostMapping("/guardar")
    public String save(@RequestParam(required = false) Integer id, @ModelAttribute Recepcionista dato, @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        if (!Acceso.post(session, token)) return "redirect:/recepcionistas";
        boolean nuevo = id == null;
        Recepcionista actual = nuevo ? new Recepcionista() : recepcionistaService.findById(id).orElse(null);
        if (actual == null || (!nuevo && !"RECEPCIONISTA".equals(actual.getRol().getNombre()))) return "redirect:/recepcionistas";
        String error = copiar(actual, dato, nuevo, session);
        if (error != null) {
            model.addAttribute("error", error);
            return formulario(actual, model);
        }
        try {
            recepcionistaService.save(actual);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se pudo guardar. Revisa datos duplicados y campos obligatorios.");
            return formulario(actual, model);
        }
        return "redirect:/recepcionistas";
    }

    @PostMapping("/eliminar/{id}")
    public String deleteById(@PathVariable Integer id, @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        if (!Acceso.post(session, token)) return "redirect:/recepcionistas";
        Recepcionista actual = recepcionistaService.findById(id).orElse(null);
        if (actual == null || !"RECEPCIONISTA".equals(actual.getRol().getNombre())) return "redirect:/recepcionistas";
        try {
            recepcionistaService.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se puede eliminar porque tiene registros relacionados.");
            cargarLista(model);
            return "recepcionistas";
        }
        return "redirect:/recepcionistas";
    }

    private void cargarLista(Model model) {
        List<Recepcionista> lista = new ArrayList<>();
        for (Recepcionista r : recepcionistaService.findAll()) {
            if ("RECEPCIONISTA".equals(r.getRol().getNombre())) lista.add(r);
        }
        model.addAttribute("lista", lista);
    }

    private String formulario(Recepcionista dato, Model model) {
        model.addAttribute("dato", dato);
        return "recepcionistas-form";
    }

    private String copiar(Recepcionista actual, Recepcionista dato, boolean nuevo, HttpSession session) {
        if (dato.getNom_recepcion() == null || dato.getNom_recepcion().isBlank() || dato.getCorreo_recepcion() == null || dato.getCorreo_recepcion().isBlank()) return "Nombre y correo obligatorios.";
        if (nuevo && (dato.getContra_recepcion() == null || dato.getContra_recepcion().isBlank())) return "La contraseña es obligatoria.";
        if (dato.getContra_recepcion() != null && !dato.getContra_recepcion().isBlank()) actual.setContra_recepcion(dato.getContra_recepcion());
        Rol rol = rolService.findByNombre("RECEPCIONISTA");
        if (rol == null) return "Falta el rol RECEPCIONISTA en la base de datos.";
        actual.setRol(rol);
        actual.setNom_recepcion(dato.getNom_recepcion());
        actual.setNum_calle_r(dato.getNum_calle_r());
        actual.setCol_r(dato.getCol_r());
        actual.setEdo_r(dato.getEdo_r());
        actual.setCod_post_r(dato.getCod_post_r());
        actual.setTel_recepcion(dato.getTel_recepcion());
        actual.setCorreo_recepcion(dato.getCorreo_recepcion());
        return null;
    }
}
