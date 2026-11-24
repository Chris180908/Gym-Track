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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController
{
    private final UsuarioService usuarioService;
    private final SesionService sesionService;
    private final RegistroService registroService;

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody RegistroUsuario datos, HttpSession session)
    {
        Recepcionista responsable = sesionService.obtenerRecepcionista(session);
        Usuario usuario = registroService.registrarUsuario(datos, responsable);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody CambioUsuario datos, HttpSession session)
    {
        Recepcionista responsable = sesionService.obtenerRecepcionista(session);
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(registroService.actualizarUsuario(usuario, datos, responsable.getId_recepcion()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session)
    {
        Recepcionista responsable = sesionService.obtenerRecepcionista(session);
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        registroService.desactivarUsuario(usuario, responsable.getId_recepcion());

        return ResponseEntity.noContent().build();
    }
}
