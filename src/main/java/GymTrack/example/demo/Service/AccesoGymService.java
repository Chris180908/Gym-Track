package GymTrack.example.demo.Service;

import GymTrack.example.demo.model.AccesoGym;

import java.util.List;
import java.util.Optional;

public interface AccesoGymService
{
    List<AccesoGym> findAll();

    Optional<AccesoGym> findById(Long id);

    AccesoGym save(AccesoGym accesoGym);

    void deleteById(Long id);

    boolean existsById(Long id);
}
