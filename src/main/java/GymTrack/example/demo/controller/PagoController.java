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
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {
    private final PagoService pagoService;
    private final MembresiaService membresiaService;
    private final RecepcionistaService recepcionistaService;

    @GetMapping
    public String findAll(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        cargarLista(model);
        return "pagos";
    }

    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        return formulario(new Pago(), model);
    }

    @GetMapping("/editar/{id}")
    public String findById(@PathVariable Integer id, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        Pago actual = pagoService.findById(id).orElse(null);
        if (actual == null) return "redirect:/pagos";
        return formulario(actual, model);
    }

    @PostMapping("/guardar")
    public String save(@RequestParam(required = false) Integer id, @ModelAttribute Pago dato, @RequestParam(required = false) Integer idMembresia, @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.post(session, token)) return "redirect:/pagos";
        boolean nuevo = id == null;
        Pago actual = nuevo ? new Pago() : pagoService.findById(id).orElse(null);
        if (actual == null) return "redirect:/pagos";
        String error = copiar(actual, dato, nuevo, session, idMembresia);
        if (error != null) {
            model.addAttribute("error", error);
            return formulario(actual, model);
        }
        try {
            pagoService.save(actual);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se pudo guardar. Revisa datos duplicados y campos obligatorios.");
            return formulario(actual, model);
        }
        return "redirect:/pagos";
    }

    @PostMapping("/eliminar/{id}")
    public String deleteById(@PathVariable Integer id, @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.post(session, token)) return "redirect:/pagos";
        Pago actual = pagoService.findById(id).orElse(null);
        if (actual == null) return "redirect:/pagos";
        try {
            pagoService.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se puede eliminar porque tiene registros relacionados.");
            cargarLista(model);
            return "pagos";
        }
        return "redirect:/pagos";
    }

    private void cargarLista(Model model) {
        model.addAttribute("lista", pagoService.findAll());
    }

    private String formulario(Pago dato, Model model) {
        model.addAttribute("dato", dato);
        model.addAttribute("membresias", membresiaService.findAll());
        return "pagos-form";
    }

    private String copiar(Pago actual, Pago dato, boolean nuevo, HttpSession session, Integer idMembresia) {
        if (idMembresia == null || dato.getMonto() == null || dato.getMonto().signum() <= 0 || dato.getConcepto() == null || dato.getConcepto().isBlank()) return "Completa membresía, monto y concepto.";
        if (!"EFECTIVO".equals(dato.getMetodo_pago()) && !"TARJETA".equals(dato.getMetodo_pago()) && !"TRANSFERENCIA".equals(dato.getMetodo_pago())) return "Método de pago inválido.";
        Membresia membresia = membresiaService.findById(idMembresia).orElse(null);
        if (membresia == null) return "Membresía inexistente.";
        actual.setMembresia(membresia);
        if (nuevo) {
            Recepcionista recepcionista = recepcionistaService.findById(Acceso.id(session)).orElse(null);
            if (recepcionista == null) return "Personal responsable inexistente.";
            actual.setRecepcionista(recepcionista);
            actual.setFecha_pago(java.time.LocalDateTime.now());
        }
        actual.setMonto(dato.getMonto());
        actual.setMetodo_pago(dato.getMetodo_pago());
        actual.setConcepto(dato.getConcepto());
        return null;
    }
}
