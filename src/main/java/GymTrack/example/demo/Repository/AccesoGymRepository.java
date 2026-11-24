package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.AccesoGym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccesoGymRepository extends JpaRepository<AccesoGym, Long>
{

}
