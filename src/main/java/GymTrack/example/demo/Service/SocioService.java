package GymTrack.example.demo.Service;

import GymTrack.example.demo.model.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioService
{
    List<Socio> findAll();

    Optional<Socio> findById(Long id);

    Socio save(Socio socio);

    void deleteById(Long id);

    boolean existsById(Long id);
}
