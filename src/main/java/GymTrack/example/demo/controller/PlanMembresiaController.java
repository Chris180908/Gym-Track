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
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanMembresiaController
{
    private final PlanMembresiaService planMembresiaService;
    private final SesionService sesionService;

    @GetMapping
    public ResponseEntity<List<PlanMembresia>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(planMembresiaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanMembresia> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return planMembresiaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlanMembresia> create(@RequestBody PlanMembresia plan, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        plan.setId_plan(null);
        plan.setActivo(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(planMembresiaService.save(plan));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanMembresia> update(@PathVariable Long id, @RequestBody PlanMembresia datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        PlanMembresia plan = planMembresiaService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        plan.setNombre(datos.getNombre());
        plan.setPrecio(datos.getPrecio());
        plan.setDuracion_meses(datos.getDuracion_meses());
        plan.setActivo(datos.isActivo());
        return ResponseEntity.ok(planMembresiaService.save(plan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        PlanMembresia plan = planMembresiaService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        plan.setActivo(false);
        planMembresiaService.save(plan);
        return ResponseEntity.noContent().build();
    }
}
