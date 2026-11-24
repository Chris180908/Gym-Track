package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.SuscripcionRepository;
import GymTrack.example.demo.Service.SuscripcionService;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuscripcionServiceImpl implements SuscripcionService
{
    private final SuscripcionRepository suscripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Suscripcion> findAll()
    {
        return suscripcionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Suscripcion> findById(Long id)
    {
        return suscripcionRepository.findById(id);
    }

    @Override
    @Transactional
    public Suscripcion save(Suscripcion suscripcion)
    {
        if (suscripcion.getSocio() == null || suscripcion.getPlan() == null || suscripcion.getEstatus() == null)
        {
            throw new IllegalArgumentException("Faltan datos de la suscripción.");
        }

        if (suscripcion.getEstatus() == EstatusSuscripcion.ACTIVA)
        {
            throw new IllegalArgumentException("La activación automática aún no está habilitada.");
        }

        return suscripcionRepository.save(suscripcion);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        suscripcionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return suscripcionRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Suscripcion> findBySocioId(Long idSocio)
    {
        return suscripcionRepository.findBySocioId(idSocio);
    }
}
