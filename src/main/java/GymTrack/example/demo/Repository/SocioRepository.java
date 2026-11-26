package GymTrack.example.demo.Repository;
import GymTrack.example.demo.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Integer> {
    @Query("SELECT s FROM Socio s WHERE LOWER(s.correo_socio) = LOWER(:correo)")
    Optional<Socio> buscarPorCorreoMovil(@Param("correo") String correo);
}
