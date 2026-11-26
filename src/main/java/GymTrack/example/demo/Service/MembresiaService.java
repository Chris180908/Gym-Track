package GymTrack.example.demo.Service;
import GymTrack.example.demo.model.Membresia;
import java.util.List;
import java.util.Optional;

public interface MembresiaService {
    List<Membresia> findAll();
    Optional<Membresia> findById(Integer id);
    Membresia save(Membresia membresia);
    void deleteById(Integer id);
    boolean existsById(Integer id);
    List<Membresia> findBySocioId(Integer idSocio);
}
