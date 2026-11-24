package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long>
{
    Rol findByNombre(String nombre);
}
