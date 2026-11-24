package GymTrack.example.demo.controller;

import GymTrack.example.demo.Service.*;
import GymTrack.example.demo.dto.*;
import GymTrack.example.demo.model.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/suscripciones")
@RequiredArgsConstructor
public class SuscripcionController
{
    private final SuscripcionService suscripcionService;
    private final SesionService sesionService;
    private final SocioService socioService;
    private final PlanMembresiaService planMembresiaService;
    private final MembresiaService membresiaService;

    @GetMapping
    public ResponseEntity<List<Suscripcion>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(suscripcionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Suscripcion> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return suscripcionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Suscripcion> create(@RequestBody RegistroSuscripcion datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Socio socio = obtenerSocio(datos.getIdSocio());
        PlanMembresia plan = obtenerPlan(datos.getIdPlan());

        if (socio.getEstatus() != EstatusSocio.ACTIVO)
        {
            throw new IllegalArgumentException("El socio está inactivo.");
        }

        Suscripcion suscripcion = Suscripcion.builder()
                .socio(socio)
                .plan(plan)
                .estatus(EstatusSuscripcion.PENDIENTE)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(suscripcionService.save(suscripcion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Suscripcion> update(@PathVariable Long id, @RequestBody CambioSuscripcion datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Suscripcion suscripcion = suscripcionService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        verificarSuscripcionLocal(suscripcion);

        if (suscripcion.getEstatus() == EstatusSuscripcion.CANCELADA)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La suscripción ya está cancelada.");
        }

        if (datos.getEstatus() == null || datos.getEstatus() == EstatusSuscripcion.ACTIVA)
        {
            throw new IllegalArgumentException("La activación requiere una pasarela de pago confirmada.");
        }

        if (datos.getIdPlan() != null && !datos.getIdPlan().equals(suscripcion.getPlan().getId_plan()))
        {
            for (Membresia membresia : membresiaService.findBySocioId(suscripcion.getSocio().getId_socio()))
            {
                if (membresia.getSuscripcion() != null
                        && id.equals(membresia.getSuscripcion().getId_suscripcion()))
                {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "La suscripción ya tiene periodos registrados.");
                }
            }

            suscripcion.setPlan(obtenerPlan(datos.getIdPlan()));
        }
        suscripcion.setEstatus(datos.getEstatus());

        return ResponseEntity.ok(suscripcionService.save(suscripcion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Suscripcion suscripcion = suscripcionService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        verificarSuscripcionLocal(suscripcion);
        suscripcion.setEstatus(EstatusSuscripcion.CANCELADA);
        suscripcion.setProximo_cobro(null);
        suscripcionService.save(suscripcion);

        return ResponseEntity.noContent().build();
    }

    private Socio obtenerSocio(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Selecciona un socio.");
        }

        return socioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio inexistente."));
    }

    private PlanMembresia obtenerPlan(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Selecciona un plan.");
        }

        PlanMembresia plan = planMembresiaService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan inexistente."));

        if (!plan.isActivo())
        {
            throw new IllegalArgumentException("El plan está inactivo.");
        }

        return plan;
    }

    private void verificarSuscripcionLocal(Suscripcion suscripcion)
    {
        if (suscripcion.getReferencia_externa() != null || suscripcion.getProveedor() != null
                || suscripcion.getEstatus() == EstatusSuscripcion.ACTIVA)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Debe gestionarse desde el proveedor de pagos.");
        }
    }
}
