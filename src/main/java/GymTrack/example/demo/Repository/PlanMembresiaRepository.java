package GymTrack.example.demo.Repository;
import GymTrack.example.demo.model.PlanMembresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanMembresiaRepository extends JpaRepository<PlanMembresia, Integer> {
}
