package GymTrack.example.demo.controller;

import GymTrack.example.demo.Service.*;
import GymTrack.example.demo.dto.*;
import GymTrack.example.demo.model.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membresias")
@RequiredArgsConstructor
public class MembresiaController
{
    private final MembresiaService membresiaService;
    private final SesionService sesionService;
    private final VentaMembresiaService ventaMembresiaService;

    @GetMapping
    public ResponseEntity<List<Membresia>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(membresiaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membresia> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return membresiaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Membresia> create(@RequestBody CompraMembresia datos, HttpSession session)
    {
        Recepcionista recepcionista = sesionService.obtenerRecepcionista(session);
        Membresia membresia = ventaMembresiaService.registrarVenta(datos, recepcionista);
        return ResponseEntity.status(HttpStatus.CREATED).body(membresia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Membresia> update(@PathVariable Long id, @RequestBody CambioMembresia datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);

        if (!membresiaService.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ventaMembresiaService.actualizarMembresia(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);

        if (!membresiaService.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }

        ventaMembresiaService.cancelarMembresia(id);
        return ResponseEntity.noContent().build();
    }
}
