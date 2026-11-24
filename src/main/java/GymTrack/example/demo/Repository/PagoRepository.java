package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>
{
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.membresia.id_mensualidad = :idMembresia")
    long contarPorMembresia(@Param("idMembresia") Long idMembresia);

    @Query("SELECT p FROM Pago p WHERE p.membresia.socio.id_socio = :idSocio ORDER BY p.fecha_pago DESC")
    List<Pago> findBySocioId(@Param("idSocio") Long idSocio);
}
