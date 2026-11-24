package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.UsuarioRepository;
import GymTrack.example.demo.Service.UsuarioService;
import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService
{
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        if (usuario.getUsername() == null || usuario.getUsername().isBlank() || usuario.getUsername().trim().length() > 100
                || usuario.getRol() == null || usuario.getPassword() == null)
        {
            throw new IllegalArgumentException("Faltan datos de la cuenta.");
        }

        if (usuario.getId() == null)
        {
            if (usuario.getPassword().length() < 8 || usuario.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72)
            {
                throw new IllegalArgumentException("Usa una contraseña de al menos 8 caracteres y máximo 72 bytes.");
            }

            usuario.setUsername(usuario.getUsername().trim());
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizarCredenciales(Usuario usuario, String username, String password)
    {
        if (username != null)
        {
            if (username.isBlank() || username.trim().length() > 100)
            {
                throw new IllegalArgumentException("El nombre de usuario no es válido.");
            }

            usuario.setUsername(username.trim());
        }

        if (password != null)
        {
            if (password.length() < 8
                    || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72)
            {
                throw new IllegalArgumentException("Usa una contraseña de 8 caracteres como mínimo y 72 bytes como máximo.");
            }

            usuario.setPassword(passwordEncoder.encode(password));
        }

        return save(usuario);
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
    @Transactional(readOnly = true)
    public Usuario findByLogin(Login login)
    {
        if (login == null || login.getUsername() == null || login.getPassword() == null)
        {
            return null;
        }

        Usuario usuario = usuarioRepository.findByUsername(login.getUsername().trim());

        if (usuario == null || !usuario.isActivo())
        {
            return null;
        }

        if (!passwordEncoder.matches(login.getPassword(), usuario.getPassword()))
        {
            return null;
        }

        return usuario;
    }
}
