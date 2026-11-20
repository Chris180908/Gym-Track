package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long>
{
    @Query("SELECT r FROM Recepcionista r WHERE r.correo_recepcion = :correo")
    Recepcionista findByCorreo_recepcion(@Param("correo") String correoRecepcion);
}
