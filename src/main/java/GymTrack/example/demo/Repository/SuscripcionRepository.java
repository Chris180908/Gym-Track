package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long>
{
    @Query("SELECT s FROM Suscripcion s WHERE s.socio.id_socio = :idSocio")
    List<Suscripcion> findBySocioId(@Param("idSocio") Long idSocio);
}
