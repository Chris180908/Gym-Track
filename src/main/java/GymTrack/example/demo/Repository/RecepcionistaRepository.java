package GymTrack.example.demo.Repository;
import GymTrack.example.demo.model.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Integer> {
    @Query("SELECT r FROM Recepcionista r WHERE LOWER(r.correo_recepcion) = LOWER(:correo)")
    Recepcionista findByCorreo_recepcion(@Param("correo") String correo);
}
