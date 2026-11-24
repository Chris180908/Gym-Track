package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.Membresia;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Long>
{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Membresia m WHERE m.id_mensualidad = :id")
    Optional<Membresia> findForUpdate(@Param("id") Long id);

    @Query("SELECT m FROM Membresia m WHERE m.socio.id_socio = :idSocio ORDER BY m.fecha_f DESC")
    List<Membresia> findBySocioId(@Param("idSocio") Long idSocio);
}
