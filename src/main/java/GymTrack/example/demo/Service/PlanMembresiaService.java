package GymTrack.example.demo.Service;
import GymTrack.example.demo.model.PlanMembresia;
import java.util.List;
import java.util.Optional;

public interface PlanMembresiaService {
    List<PlanMembresia> findAll();
    Optional<PlanMembresia> findById(Integer id);
    PlanMembresia save(PlanMembresia planMembresia);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
