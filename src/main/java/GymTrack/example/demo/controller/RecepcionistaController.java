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
@RequestMapping("/api/recepcionistas")
@RequiredArgsConstructor
public class RecepcionistaController
{
    private final RecepcionistaService recepcionistaService;
    private final SesionService sesionService;
    private final RegistroService registroService;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Recepcionista>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(recepcionistaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recepcionista> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return recepcionistaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Recepcionista> create(@RequestBody RegistroSocio datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Recepcionista recepcionista = registroService.registrarRecepcionista(datos);

        return ResponseEntity.status(HttpStatus.CREATED).body(recepcionista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recepcionista> update(@PathVariable Long id, @RequestBody DatosSocio datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Recepcionista recepcionista = recepcionistaService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(registroService.actualizarRecepcionista(recepcionista, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session)
    {
        Recepcionista responsable = sesionService.obtenerRecepcionista(session);

        if (!recepcionistaService.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        registroService.desactivarUsuario(usuario, responsable.getId_recepcion());

        return ResponseEntity.noContent().build();
    }
}
