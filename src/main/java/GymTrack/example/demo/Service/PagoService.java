package GymTrack.example.demo.Service;
import GymTrack.example.demo.model.Pago;
import java.util.List;
import java.util.Optional;

public interface PagoService {
    List<Pago> findAll();
    Optional<Pago> findById(Integer id);
    Pago save(Pago pago);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
