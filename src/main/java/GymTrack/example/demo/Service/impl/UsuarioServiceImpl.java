package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.Usuario;
import GymTrack.example.demo.Repository.UsuarioRepository;
import GymTrack.example.demo.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService
{
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll()
    {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id)
    {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario)
    {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return usuarioRepository.existsById(id);
    }

    @Override
    public Usuario findByLogin(Login login)
    {
        Usuario usuario = usuarioRepository.findByUsername( login.getUsername( ) );
        if( usuario == null )
        {
            return null;
        }
        if( usuario.getPassword( ).equals( login.getPassword( ) ) )
        {
            return usuario;
        }
        return null;
    }
}