package GymTrack.example.demo.Service;

import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService
{
    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Usuario save(Usuario usuario);

    void deleteById(Long id);

    boolean existsById(Long id);

    Usuario actualizarCredenciales(Usuario usuario, String username, String password);

    Usuario findByLogin(Login login);
}
