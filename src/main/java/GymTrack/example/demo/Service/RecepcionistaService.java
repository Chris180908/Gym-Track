package GymTrack.example.demo.Service;
import GymTrack.example.demo.model.Recepcionista;
import GymTrack.example.demo.dto.Login;
import java.util.List;
import java.util.Optional;

public interface RecepcionistaService {
    List<Recepcionista> findAll();
    Optional<Recepcionista> findById(Integer id);
    Recepcionista save(Recepcionista recepcionista);
    void deleteById(Integer id);
    boolean existsById(Integer id);
    Recepcionista findByLogin(Login login);
}
