package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.RecepcionistaRepository;
import GymTrack.example.demo.Repository.SocioRepository;
import GymTrack.example.demo.Repository.UsuarioRepository;
import GymTrack.example.demo.Service.RecepcionistaService;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecepcionistaServiceImpl implements RecepcionistaService
{
    private final RecepcionistaRepository recepcionistaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SocioRepository socioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Recepcionista> findAll()
    {
        return recepcionistaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Recepcionista> findById(Long id)
    {
        return recepcionistaRepository.findById(id);
    }

    @Override
    @Transactional
    public Recepcionista save(Recepcionista recepcionista)
    {
        if (recepcionista.getId_recepcion() == null || recepcionista.getNom_recepcion() == null
                || recepcionista.getNom_recepcion().isBlank())
        {
            throw new IllegalArgumentException("Faltan datos del recepcionista.");
        }

        Usuario cuenta = usuarioRepository.findById(recepcionista.getId_recepcion()).orElseThrow();

        if (!"RECEPCIONISTA".equals(cuenta.getRol().getNombre()) || socioRepository.existsById(cuenta.getId()))
        {
            throw new IllegalArgumentException("La cuenta no corresponde a un recepcionista.");
        }

        return recepcionistaRepository.save(recepcionista);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        recepcionistaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return recepcionistaRepository.existsById(id);
    }
}
