package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.RecepcionistaRepository;
import GymTrack.example.demo.Repository.SocioRepository;
import GymTrack.example.demo.Repository.UsuarioRepository;
import GymTrack.example.demo.Service.SocioService;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocioServiceImpl implements SocioService
{
    private final SocioRepository socioRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecepcionistaRepository recepcionistaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Socio> findAll()
    {
        return socioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Socio> findById(Long id)
    {
        return socioRepository.findById(id);
    }

    @Override
    @Transactional
    public Socio save(Socio socio)
    {
        if (socio.getId_socio() == null || socio.getNom_socio() == null || socio.getNom_socio().isBlank()
                || socio.getRecepcionista() == null || socio.getFecha_registro() == null || socio.getEstatus() == null)
        {
            throw new IllegalArgumentException("Faltan datos del socio.");
        }

        Usuario cuenta = usuarioRepository.findById(socio.getId_socio()).orElseThrow();

        if (!"SOCIO".equals(cuenta.getRol().getNombre()) || recepcionistaRepository.existsById(cuenta.getId()))
        {
            throw new IllegalArgumentException("La cuenta no corresponde a un socio.");
        }

        return socioRepository.save(socio);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        socioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return socioRepository.existsById(id);
    }
}
