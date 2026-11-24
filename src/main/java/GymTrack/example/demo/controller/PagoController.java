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
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController
{
    private final PagoService pagoService;
    private final SesionService sesionService;
    private final VentaMembresiaService ventaMembresiaService;

    @GetMapping
    public ResponseEntity<List<Pago>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(pagoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return pagoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pago> create(@RequestBody RegistroPago datos, HttpSession session)
    {
        Recepcionista recepcionista = sesionService.obtenerRecepcionista(session);
        Pago pago = ventaMembresiaService.registrarPagoPendiente(datos, recepcionista);

        return ResponseEntity.status(HttpStatus.CREATED).body(pago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> update(@PathVariable Long id, @RequestBody CambioPago datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Pago pago = pagoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (pago.getOrigen() != OrigenPago.RECEPCION)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se modifican pagos externos.");
        }

        if (datos.getConcepto() == null || datos.getConcepto().isBlank() || datos.getConcepto().length() > 100)
        {
            throw new IllegalArgumentException("El concepto es obligatorio y admite hasta 100 caracteres.");
        }

        // No se alteran el monto, la fecha, el método ni la membresía pagada.
        pago.setConcepto(datos.getConcepto());

        return ResponseEntity.ok(pagoService.save(pago));
    }
}
