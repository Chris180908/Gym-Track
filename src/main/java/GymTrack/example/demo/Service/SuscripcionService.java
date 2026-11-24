package GymTrack.example.demo.Service;

import GymTrack.example.demo.model.Suscripcion;

import java.util.List;
import java.util.Optional;

public interface SuscripcionService
{
    List<Suscripcion> findAll();

    Optional<Suscripcion> findById(Long id);

    Suscripcion save(Suscripcion suscripcion);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Suscripcion> findBySocioId(Long idSocio);
}
