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
@RequestMapping("/api/socios")
@RequiredArgsConstructor
public class SocioController
{
    private final SocioService socioService;
    private final RegistroService registroService;
    private final SesionService sesionService;

    @GetMapping
    public ResponseEntity<List<Socio>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(socioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Socio> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return socioService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Socio> create(@RequestBody RegistroSocio datos, HttpSession session)
    {
        Recepcionista recepcionista = sesionService.obtenerRecepcionista(session);
        Socio socio = registroService.registrarSocio(datos, recepcionista);
        return ResponseEntity.status(HttpStatus.CREATED).body(socio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Socio> update(@PathVariable Long id, @RequestBody DatosSocio datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Socio socio = socioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(registroService.actualizarSocio(socio, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Socio socio = socioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        registroService.desactivarSocio(socio);
        return ResponseEntity.noContent().build();
    }
}
