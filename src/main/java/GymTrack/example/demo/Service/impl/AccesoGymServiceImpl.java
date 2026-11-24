package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.AccesoGymRepository;
import GymTrack.example.demo.Service.AccesoGymService;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccesoGymServiceImpl implements AccesoGymService
{
    private final AccesoGymRepository accesoGymRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AccesoGym> findAll()
    {
        return accesoGymRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccesoGym> findById(Long id)
    {
        return accesoGymRepository.findById(id);
    }

    @Override
    @Transactional
    public AccesoGym save(AccesoGym accesoGym)
    {
        if (accesoGym.getSocio() == null || accesoGym.getResultado() == null)
        {
            throw new IllegalArgumentException("Faltan datos del acceso.");
        }

        if (accesoGym.getFecha_acceso() == null)
        {
            accesoGym.setFecha_acceso(LocalDateTime.now());
        }

        return accesoGymRepository.save(accesoGym);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        accesoGymRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return accesoGymRepository.existsById(id);
    }
}
