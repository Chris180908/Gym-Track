package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long>
{
    @Query("SELECT r FROM Socio r WHERE r.correo_socio = :correo")
    Socio findByCorreo_socio(@Param("correo") String correoRecepcion);
}
