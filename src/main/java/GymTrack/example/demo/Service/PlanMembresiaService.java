package GymTrack.example.demo.Service;

import GymTrack.example.demo.model.PlanMembresia;

import java.util.List;
import java.util.Optional;

public interface PlanMembresiaService
{
    List<PlanMembresia> findAll();

    Optional<PlanMembresia> findById(Long id);

    PlanMembresia save(PlanMembresia planMembresia);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<PlanMembresia> findActivos();
}
