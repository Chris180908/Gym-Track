package GymTrack.example.demo.Service;
import GymTrack.example.demo.model.Rol;
import java.util.List;
import java.util.Optional;

public interface RolService {
    List<Rol> findAll();
    Optional<Rol> findById(Integer id);
    Rol save(Rol rol);
    void deleteById(Integer id);
    boolean existsById(Integer id);
    Rol findByNombre(String nombre);
}
