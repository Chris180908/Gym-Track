package GymTrack.example.demo.controller;

import GymTrack.example.demo.Service.*;
import GymTrack.example.demo.model.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class VistaController {

    private final SocioService socioService;
    private final RecepcionistaService recepcionistaService;
    private final MembresiaService membresiaService;
    private final PagoService pagoService;

    @GetMapping("/inicio")
    public String inicio(@RequestParam(required = false) String q, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";

        LocalDate hoy = LocalDate.now();
        List<Socio> socios = socioService.findAll();

        int activos = 0;
        for (Socio s : socios) {
            if ("ACTIVO".equals(s.getEstatus())) activos++;
        }

        int vigentes = 0;
        List<Membresia> porVencer = new ArrayList<>();
        for (Membresia m : membresiaService.findAll()) {
            boolean vigenteHoy = "VIGENTE".equals(m.getEstatus())
                    && !hoy.isBefore(m.getFecha_i())
                    && !hoy.isAfter(m.getFecha_f());
            if (!vigenteHoy) continue;
            vigentes++;
            if (!m.getFecha_f().isAfter(hoy.plusDays(7))) porVencer.add(m);
        }
        porVencer.sort(Comparator.comparing(Membresia::getFecha_f));

        BigDecimal ingresosMes = BigDecimal.ZERO;
        List<Pago> pagos = new ArrayList<>(pagoService.findAll());
        for (Pago p : pagos) {
            LocalDate fecha = p.getFecha_pago().toLocalDate();
            if (fecha.getYear() == hoy.getYear() && fecha.getMonth() == hoy.getMonth()) {
                ingresosMes = ingresosMes.add(p.getMonto());
            }
        }
        pagos.sort(Comparator.comparing(Pago::getFecha_pago).reversed());

        model.addAttribute("totalSocios", socios.size());
        model.addAttribute("activos", activos);
        model.addAttribute("membresiasVigentes", vigentes);
        model.addAttribute("vencen7", porVencer.size());
        model.addAttribute("ingresosMes", ingresosMes);
        model.addAttribute("porVencer", porVencer);
        model.addAttribute("ultimosPagos", pagos.subList(0, Math.min(5, pagos.size())));
        model.addAttribute("hoyTexto", hoy.format(
                DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "MX"))));

        // Búsqueda rápida por ID, nombre, correo o teléfono
        if (q != null && !q.isBlank()) {
            String t = q.trim().toLowerCase();
            List<Socio> encontrados = new ArrayList<>();
            for (Socio s : socios) {
                boolean coincide = String.valueOf(s.getId_socio()).equals(t)
                        || (s.getNom_socio() != null && s.getNom_socio().toLowerCase().contains(t))
                        || (s.getCorreo_socio() != null && s.getCorreo_socio().toLowerCase().contains(t))
                        || (s.getTel_socio() != null && s.getTel_socio().contains(t));
                if (coincide) encontrados.add(s);
            }
            model.addAttribute("q", q.trim());
            model.addAttribute("resultados", encontrados);
        }
        return "inicio";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        Recepcionista dato = recepcionistaService.findById(Acceso.id(session)).orElse(null);
        if (dato == null) {
            session.invalidate();
            return "redirect:/login";
        }
        model.addAttribute("dato", dato);
        return "perfil";
    }

    @PostMapping("/perfil")
    public String editarPerfil(@ModelAttribute Recepcionista dato, @RequestParam String token,
                               HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.post(session, token)) return "redirect:/perfil";

        Recepcionista actual = recepcionistaService.findById(Acceso.id(session)).orElse(null);
        if (actual == null) {
            session.invalidate();
            return "redirect:/login";
        }

        // Solo el ADMIN (dueño) puede cambiar su nombre y correo
        if (Acceso.admin(session)) {
            if (dato.getNom_recepcion() == null || dato.getNom_recepcion().isBlank()
                    || dato.getCorreo_recepcion() == null || dato.getCorreo_recepcion().isBlank()) {
                model.addAttribute("error", "Nombre y correo obligatorios.");
                model.addAttribute("dato", actual);
                return "perfil";
            }
            actual.setNom_recepcion(dato.getNom_recepcion());
            actual.setCorreo_recepcion(dato.getCorreo_recepcion());
        }

        actual.setTel_recepcion(dato.getTel_recepcion());
        actual.setNum_calle_r(dato.getNum_calle_r());
        actual.setCol_r(dato.getCol_r());
        actual.setEdo_r(dato.getEdo_r());
        actual.setCod_post_r(dato.getCod_post_r());

        try {
            recepcionistaService.save(actual);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Ese correo ya está en uso.");
            model.addAttribute("dato", actual);
            return "perfil";
        }

        session.setAttribute("nombre", actual.getNom_recepcion()); // actualiza el nombre del sidebar
        return "redirect:/perfil";
    }
}