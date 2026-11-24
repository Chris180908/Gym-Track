package GymTrack.example.demo.controller;

import GymTrack.example.demo.Service.*;
import GymTrack.example.demo.dto.*;
import GymTrack.example.demo.model.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movil")
@RequiredArgsConstructor
public class MovilController
{
    private final SesionService sesionService;
    private final RegistroService registroService;
    private final MembresiaService membresiaService;
    private final PagoService pagoService;
    private final PlanMembresiaService planMembresiaService;

    @GetMapping("/perfil")
    public ResponseEntity<SocioResponse> perfil(HttpSession session)
    {
        return ResponseEntity.ok(convertirSocio(sesionService.obtenerSocio(session)));
    }

    @PutMapping("/perfil")
    public ResponseEntity<SocioResponse> actualizarPerfil(@RequestBody DatosSocio datos, HttpSession session)
    {
        Socio socio = sesionService.obtenerSocio(session);
        socio = registroService.actualizarSocio(socio, datos);
        return ResponseEntity.ok(convertirSocio(socio));
    }

    @GetMapping("/membresias")
    public ResponseEntity<List<MembresiaResponse>> membresias(HttpSession session)
    {
        Socio socio = sesionService.obtenerSocio(session);
        List<MembresiaResponse> respuesta = new java.util.ArrayList<>();

        for (Membresia m : membresiaService.findBySocioId(socio.getId_socio()))
        {
            String estado = m.getEstatusActual();

            respuesta.add(new MembresiaResponse(m.getId_mensualidad(), m.getTipo(), m.getPrecio(),
                    m.getFecha_i(), m.getFecha_f(), estado, m.getModalidad().name()));
        }

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/pagos")
    public ResponseEntity<List<PagoResponse>> pagos(HttpSession session)
    {
        Socio socio = sesionService.obtenerSocio(session);
        List<PagoResponse> respuesta = new java.util.ArrayList<>();

        for (Pago p : pagoService.findBySocioId(socio.getId_socio()))
        {
            respuesta.add(new PagoResponse(p.getId_pago(), p.getMonto(), p.getFecha_pago(),
                    p.getMetodo_pago().name(), p.getConcepto(), p.getOrigen().name(), p.getMoneda()));
        }

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/planes")
    public ResponseEntity<List<PlanMembresia>> planes(HttpSession session)
    {
        sesionService.obtenerSocio(session);
        return ResponseEntity.ok(planMembresiaService.findActivos());
    }

    private SocioResponse convertirSocio(Socio socio)
    {
        return new SocioResponse(socio.getId_socio(), socio.getNom_socio(), socio.getTel_socio(),
                socio.getCorreo_socio(), socio.getNum_calle_s(), socio.getCol_s(), socio.getEdo_s(),
                socio.getCod_post_s(), socio.getEstatus().name(), socio.getFecha_registro());
    }
}
