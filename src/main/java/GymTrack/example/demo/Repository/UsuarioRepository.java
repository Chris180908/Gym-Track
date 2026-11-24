package GymTrack.example.demo.Repository;

import GymTrack.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{
    Usuario findByUsername(String username);
}
