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
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController
{
    private final ReporteService reporteService;
    private final SesionService sesionService;

    @GetMapping
    public ResponseEntity<List<Reporte>> findAll(HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return ResponseEntity.ok(reporteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> findById(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        return reporteService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reporte> create(@RequestBody RegistroReporte datos, HttpSession session)
    {
        Recepcionista recepcionista = sesionService.obtenerRecepcionista(session);
        Reporte reporte = Reporte.builder()
                .tipo_reporte(datos.getTipo())
                .fecha_inicio_filtro(datos.getFechaInicio())
                .fecha_fin_filtro(datos.getFechaFin())
                .recepcionista(recepcionista)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.save(reporte));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> update(@PathVariable Long id, @RequestBody RegistroReporte datos, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);
        Reporte reporte = reporteService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        reporte.setTipo_reporte(datos.getTipo());
        reporte.setFecha_inicio_filtro(datos.getFechaInicio());
        reporte.setFecha_fin_filtro(datos.getFechaFin());

        return ResponseEntity.ok(reporteService.save(reporte));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpSession session)
    {
        sesionService.obtenerRecepcionista(session);

        if (!reporteService.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }

        reporteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
