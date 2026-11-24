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
@RequestMapping("/api/accesos")
@RequiredArgsConstructor
public class AccesoGymController
{
    private final AccesoGymService accesoGymService;
    private final SesionService sesionService;
    private final SocioService socioService;
    private final MembresiaService membresiaService;
    private final UsuarioService usuarioService;
    private final PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<AccesoGym>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(accesoGymService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccesoGym> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return accesoGymService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccesoGym> create(@RequestBody RegistroAcceso datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);

        if (datos.getIdSocio() == null)
        {
            throw new IllegalArgumentException("Selecciona un socio.");
        }

        Socio socio = socioService.findById(datos.getIdSocio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Usuario usuario = usuarioService.findById(datos.getIdSocio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ResultadoAcceso resultado = ResultadoAcceso.DENEGADO;

        if (usuario.isActivo() && socio.getEstatus() == EstatusSocio.ACTIVO)
        {
            for (Membresia membresia : membresiaService.findBySocioId(socio.getId_socio()))
            {
                if ("VIGENTE".equals(membresia.getEstatusActual())
                        && pagoService.existePagoDeMembresia(membresia.getId_mensualidad()))
                {
                    resultado = ResultadoAcceso.PERMITIDO;
                    break;
                }
            }
        }

        // Validación manual de prueba; todavía no existe un lector QR conectado.
        AccesoGym acceso = AccesoGym.builder()
                .socio(socio)
                .resultado(resultado)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(accesoGymService.save(acceso));
    }
}
