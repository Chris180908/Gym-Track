package GymTrack.example.demo.Service;

import GymTrack.example.demo.model.Membresia;

import java.util.List;
import java.util.Optional;

public interface MembresiaService
{
    List<Membresia> findAll();

    Optional<Membresia> findById(Long id);

    Membresia save(Membresia membresia);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Membresia> findBySocioId(Long idSocio);
}
