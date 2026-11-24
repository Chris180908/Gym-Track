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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController
{
    private final RolService rolService;
    private final SesionService sesionService;

    @GetMapping
    public ResponseEntity<List<Rol>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return rolService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rol> create(@RequestBody Rol rol, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        rol.setId_rol(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.save(rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> update(@PathVariable Long id, @RequestBody Rol datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Rol rol = rolService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (esRolDelSistema(rol) && !rol.getNombre().equals(datos.getNombre()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No puedes renombrar un rol del sistema.");
        }

        rol.setNombre(datos.getNombre());
        rol.setDescripcion(datos.getDescripcion());

        return ResponseEntity.ok(rolService.save(rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Rol rol = rolService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (esRolDelSistema(rol))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No puedes eliminar un rol del sistema.");
        }

        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean esRolDelSistema(Rol rol)
    {
        return "SOCIO".equals(rol.getNombre()) || "RECEPCIONISTA".equals(rol.getNombre());
    }
}
