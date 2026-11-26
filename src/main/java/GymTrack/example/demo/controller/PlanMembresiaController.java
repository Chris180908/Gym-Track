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
@RequestMapping("/planes")
@RequiredArgsConstructor
public class PlanMembresiaController {
    private final RecepcionistaService recepcionistaService;
    private final PlanMembresiaService planMembresiaService;

    @GetMapping
    public String findAll(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        cargarLista(model);
        return "planes";
    }

    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        return formulario(new PlanMembresia(), model);
    }

    @GetMapping("/editar/{id}")
    public String findById(@PathVariable Integer id, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        PlanMembresia actual = planMembresiaService.findById(id).orElse(null);
        if (actual == null) return "redirect:/planes";
        return formulario(actual, model);
    }

    @PostMapping("/guardar")
    public String save(@RequestParam(required = false) Integer id, @ModelAttribute PlanMembresia dato, @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        if (!Acceso.post(session, token)) return "redirect:/planes";
        boolean nuevo = id == null;
        PlanMembresia actual = nuevo ? new PlanMembresia() : planMembresiaService.findById(id).orElse(null);
        if (actual == null) return "redirect:/planes";
        String error = copiar(actual, dato, nuevo, session);
        if (error != null) {
            model.addAttribute("error", error);
            return formulario(actual, model);
        }
        try {
            planMembresiaService.save(actual);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se pudo guardar. Revisa datos duplicados y campos obligatorios.");
            return formulario(actual, model);
        }
        return "redirect:/planes";
    }

    @PostMapping("/eliminar/{id}")
    public String deleteById(@PathVariable Integer id, @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        if (!Acceso.post(session, token)) return "redirect:/planes";
        PlanMembresia actual = planMembresiaService.findById(id).orElse(null);
        if (actual == null) return "redirect:/planes";
        try {
            planMembresiaService.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se puede eliminar porque tiene registros relacionados.");
            cargarLista(model);
            return "planes";
        }
        return "redirect:/planes";
    }

    private void cargarLista(Model model) {
        model.addAttribute("lista", planMembresiaService.findAll());
    }

    private String formulario(PlanMembresia dato, Model model) {
        model.addAttribute("dato", dato);
        return "planes-form";
    }

    private String copiar(PlanMembresia actual, PlanMembresia dato, boolean nuevo, HttpSession session) {
        if (dato.getNombre() == null || dato.getNombre().isBlank() || dato.getDuracion_dias() == null || dato.getDuracion_dias() <= 0 || dato.getPrecio() == null || dato.getPrecio().signum() < 0 || dato.getActivo() == null) return "Revisa el nombre, la duración y el precio.";
        actual.setNombre(dato.getNombre());
        actual.setDuracion_dias(dato.getDuracion_dias());
        actual.setPrecio(dato.getPrecio());
        actual.setActivo(dato.getActivo());
        return null;
    }
}
