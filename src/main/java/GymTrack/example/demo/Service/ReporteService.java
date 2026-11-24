package GymTrack.example.demo.Service;

import GymTrack.example.demo.model.Reporte;

import java.util.List;
import java.util.Optional;

public interface ReporteService
{
    List<Reporte> findAll();

    Optional<Reporte> findById(Long id);

    Reporte save(Reporte reporte);

    void deleteById(Long id);

    boolean existsById(Long id);
}
