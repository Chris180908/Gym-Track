package GymTrack.example.demo.Service;
import GymTrack.example.demo.model.Socio;
import java.util.List;
import java.util.Optional;

public interface SocioService {
    List<Socio> findAll();
    Optional<Socio> findById(Integer id);
    Socio save(Socio socio);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
