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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

@Controller
@RequestMapping("/membresias")
@RequiredArgsConstructor
public class MembresiaController {

    private static final List<String> METODOS = List.of("EFECTIVO", "TARJETA", "TRANSFERENCIA");

    private final RecepcionistaService recepcionistaService;
    private final MembresiaService membresiaService;
    private final SocioService socioService;
    private final PlanMembresiaService planMembresiaService;
    private final PagoService pagoService;

    @GetMapping
    public String findAll(@RequestParam(required = false) Integer anio, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        cargarLista(model, anio);
        return "membresias";
    }

    @GetMapping("/nuevo")
    public String nuevo(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        return formulario(new Membresia(), model);
    }

    @GetMapping("/editar/{id}")
    public String findById(@PathVariable Integer id, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        Membresia actual = membresiaService.findById(id).orElse(null);
        if (actual == null) return "redirect:/membresias";
        return formulario(actual, model);
    }

    @PostMapping("/guardar")
    public String save(@RequestParam(required = false) Integer id,
                       @ModelAttribute Membresia dato,
                       @RequestParam(required = false) Integer idSocio,
                       @RequestParam(required = false) Integer idPlan,
                       @RequestParam(required = false) String metodoPago,
                       @RequestParam String token,
                       HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.post(session, token)) return "redirect:/membresias";

        boolean nuevo = id == null;
        Membresia actual = nuevo ? new Membresia() : membresiaService.findById(id).orElse(null);
        if (actual == null) return "redirect:/membresias";

        String error = copiar(actual, dato, nuevo, idSocio, idPlan);
        if (error != null) {
            model.addAttribute("error", error);
            return formulario(actual, model);
        }

        // Al crear siempre se cobra; al editar (renovar) el cobro es opcional.
        boolean cobrar = nuevo || (metodoPago != null && !metodoPago.isBlank());
        if (cobrar && !METODOS.contains(metodoPago)) {
            model.addAttribute("error", "Selecciona el método de pago.");
            return formulario(actual, model);
        }

        try {
            membresiaService.save(actual);
            if (cobrar) registrarPago(actual, metodoPago, session, nuevo ? "Membresía " : "Renovación ");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se pudo guardar. Revisa datos duplicados y campos obligatorios.");
            return formulario(actual, model);
        }
        return "redirect:/membresias";
    }

    @PostMapping("/eliminar/{id}")
    public String deleteById(@PathVariable Integer id, @RequestParam String token, HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.post(session, token)) return "redirect:/membresias";
        if (!membresiaService.existsById(id)) return "redirect:/membresias";
        try {
            membresiaService.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No se puede eliminar porque tiene registros relacionados.");
            cargarLista(model, null);
            return "membresias";
        }
        return "redirect:/membresias";
    }

    // ---------- Apoyo ----------

    private void cargarLista(Model model, Integer anioElegido) {
        List<Membresia> lista = membresiaService.findAll();
        model.addAttribute("lista", lista);

        LocalDate hoy = LocalDate.now();
        int anio = anioElegido == null ? hoy.getYear() : anioElegido;

        BigDecimal[] meses = new BigDecimal[12];
        Arrays.fill(meses, BigDecimal.ZERO);
        BigDecimal totalAnio = BigDecimal.ZERO;
        TreeSet<Integer> anios = new TreeSet<>(Collections.reverseOrder());
        anios.add(hoy.getYear());

        for (Pago p : pagoService.findAll()) {
            int anioPago = p.getFecha_pago().getYear();
            anios.add(anioPago);
            if (anioPago == anio) {
                int mes = p.getFecha_pago().getMonthValue() - 1;
                meses[mes] = meses[mes].add(p.getMonto());
                totalAnio = totalAnio.add(p.getMonto());
            }
        }

        int vigentes = 0;
        for (Membresia m : lista) {
            if ("VIGENTE".equals(m.getEstatus()) && !hoy.isBefore(m.getFecha_i()) && !hoy.isAfter(m.getFecha_f())) vigentes++;
        }

        model.addAttribute("datosParaJS", Arrays.toString(meses)); // ingresos de ene a dic para la gráfica
        model.addAttribute("ingresosAnio", totalAnio);
        model.addAttribute("vigentes", vigentes);
        model.addAttribute("anio", anio);
        model.addAttribute("anios", anios);
    }

    private void registrarPago(Membresia membresia, String metodoPago, HttpSession session, String prefijo) {
        Recepcionista quien = recepcionistaService.findById(Acceso.id(session)).orElse(null);
        if (quien == null) return;
        Pago pago = new Pago();
        pago.setMembresia(membresia);
        pago.setRecepcionista(quien);
        pago.setMonto(membresia.getPrecio_cobrado());
        pago.setMetodo_pago(metodoPago);
        pago.setConcepto(prefijo + membresia.getPlan().getNombre());
        pago.setFecha_pago(LocalDateTime.now());
        pagoService.save(pago);
    }

    private String formulario(Membresia dato, Model model) {
        model.addAttribute("dato", dato);
        model.addAttribute("socios", socioService.findAll());
        model.addAttribute("planes", planMembresiaService.findAll());
        return "membresias-form";
    }

    private String copiar(Membresia actual, Membresia dato, boolean nuevo, Integer idSocio, Integer idPlan) {
        if (idSocio == null || idPlan == null || dato.getFecha_i() == null) return "Selecciona socio, plan y fecha.";

        Socio socio = socioService.findById(idSocio).orElse(null);
        PlanMembresia plan = planMembresiaService.findById(idPlan).orElse(null);
        if (socio == null || plan == null) return "Socio o plan inexistente.";

        boolean cambiaSocio = nuevo || !socio.getId_socio().equals(actual.getSocio().getId_socio());
        boolean cambiaPlan = nuevo || !plan.getId_plan().equals(actual.getPlan().getId_plan());

        if (cambiaSocio && !"ACTIVO".equals(socio.getEstatus())) return "El socio está inactivo.";
        if (cambiaPlan && !Boolean.TRUE.equals(plan.getActivo())) return "El plan no está activo.";

        String estatus = dato.getEstatus();
        if (!"VIGENTE".equals(estatus) && !"VENCIDA".equals(estatus) && !"CANCELADA".equals(estatus)) return "Estatus inválido.";

        if (cambiaPlan) actual.setPrecio_cobrado(plan.getPrecio());
        if (cambiaPlan || !dato.getFecha_i().equals(actual.getFecha_i())) {
            actual.setFecha_f(dato.getFecha_i().plusDays(plan.getDuracion_dias()));
        }

        actual.setSocio(socio);
        actual.setPlan(plan);
        actual.setFecha_i(dato.getFecha_i());
        actual.setEstatus(estatus);
        return null;
    }
}