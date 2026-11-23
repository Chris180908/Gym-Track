package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Integer> {

    @Query("SELECT m FROM Membresia m WHERE m.socio.id_socio = :idSocio")
    List<Membresia> findBySocioId(@Param("idSocio") Integer idSocio);
}
