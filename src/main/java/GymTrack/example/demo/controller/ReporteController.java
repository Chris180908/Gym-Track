package GymTrack.example.demo.controller;
import GymTrack.example.demo.Service.*;
import GymTrack.example.demo.model.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final ReporteService reporteService;
    private final SocioService socioService;
    private final MembresiaService membresiaService;
    private final PagoService pagoService;
    private final RecepcionistaService recepcionistaService;

    @GetMapping
    public String findAll(@RequestParam(defaultValue = "GENERAL") String tipoReporte,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            model.addAttribute("error", "La fecha final no puede ser anterior a la inicial.");
            fechaInicio = null; fechaFin = null;
        }
        cargar(model, tipoReporte, fechaInicio, fechaFin);
        return "reportes";
    }

    @PostMapping("/guardar")
    public String save(@RequestParam(required = false) Integer id, @RequestParam String tipoReporte,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.post(session, token)) return "redirect:/reportes";
        if (!List.of("GENERAL", "SOCIOS", "MEMBRESIAS", "PAGOS").contains(tipoReporte) ||
            (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio))) {
            model.addAttribute("error", "Revisa el tipo y las fechas del reporte.");
            cargar(model, "GENERAL", null, null); return "reportes";
        }
        Recepcionista r = recepcionistaService.findById(Acceso.id(session)).orElse(null);
        if (r == null) return "redirect:/login";
        Reporte reporte = id == null ? new Reporte() : reporteService.findById(id).orElse(null);
        if (reporte == null) return "redirect:/reportes";
        reporte.setTipo_reporte(tipoReporte);
        reporte.setFecha_inicio_filtro(fechaInicio);
        reporte.setFecha_fin_filtro(fechaFin);
        if (id == null) {
            reporte.setFecha_g(LocalDateTime.now());
            reporte.setRecepcionista(r);
        }
        reporteService.save(reporte);
        model.addAttribute("mensaje", id == null ? "Reporte guardado en el historial." : "Reporte actualizado.");
        cargar(model, tipoReporte, fechaInicio, fechaFin);
        return "reportes";
    }

    @GetMapping("/ver/{id}")
    public String findById(@PathVariable Integer id, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        Reporte reporte = reporteService.findById(id).orElse(null);
        if (reporte == null) return "redirect:/reportes";
        cargar(model, reporte.getTipo_reporte(), reporte.getFecha_inicio_filtro(), reporte.getFecha_fin_filtro());
        return "reportes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        Reporte reporte = reporteService.findById(id).orElse(null);
        if (reporte == null) return "redirect:/reportes";
        model.addAttribute("idEditar", id);
        cargar(model, reporte.getTipo_reporte(), reporte.getFecha_inicio_filtro(), reporte.getFecha_fin_filtro());
        return "reportes";
    }

    @PostMapping("/eliminar/{id}")
    public String deleteById(@PathVariable Integer id, @RequestParam String token, HttpSession session) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (Acceso.post(session, token) && reporteService.existsById(id)) reporteService.deleteById(id);
        return "redirect:/reportes";
    }

    private boolean periodo(LocalDate fecha, LocalDate inicio, LocalDate fin) {
        return (inicio == null || !fecha.isBefore(inicio)) && (fin == null || !fecha.isAfter(fin));
    }
    private void cargar(Model model, String tipo, LocalDate inicio, LocalDate fin) {
        if (!List.of("GENERAL", "SOCIOS", "MEMBRESIAS", "PAGOS").contains(tipo)) tipo = "GENERAL";
        List<Socio> socios = socioService.findAll();
        List<Membresia> membresias = new ArrayList<>();
        List<Pago> pagos = new ArrayList<>();
        BigDecimal ingresos = BigDecimal.ZERO;
        int activos = 0, vigentes = 0;
        LocalDate hoy = LocalDate.now();
        for (Socio s : socios) if ("ACTIVO".equals(s.getEstatus())) activos++;
        for (Membresia m : membresiaService.findAll()) {
            if ("VIGENTE".equals(m.getEstatus()) && !hoy.isBefore(m.getFecha_i()) && !hoy.isAfter(m.getFecha_f())) vigentes++;
            if ((inicio == null || !m.getFecha_f().isBefore(inicio)) && (fin == null || !m.getFecha_i().isAfter(fin))) membresias.add(m);
        }
        for (Pago p : pagoService.findAll()) if (periodo(p.getFecha_pago().toLocalDate(), inicio, fin)) { pagos.add(p); ingresos = ingresos.add(p.getMonto()); }
        model.addAttribute("socios", socios); // SOCIO no tiene fecha de registro.
        model.addAttribute("membresias", membresias);
        model.addAttribute("pagos", pagos);
        model.addAttribute("reportes", reporteService.findAll());
        model.addAttribute("totalSocios", activos);
        model.addAttribute("membresiasVigentes", vigentes);
        model.addAttribute("ingresosTotales", ingresos);
        model.addAttribute("tipoReporte", tipo);
        model.addAttribute("fechaInicio", inicio);
        model.addAttribute("fechaFin", fin);
    }
}
