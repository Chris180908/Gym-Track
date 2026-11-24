package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.Socio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long>
{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Socio s WHERE s.id_socio = :idSocio")
    Optional<Socio> findForUpdate(@Param("idSocio") Long idSocio);
}
