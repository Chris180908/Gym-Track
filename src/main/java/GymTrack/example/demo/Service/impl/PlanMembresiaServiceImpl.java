package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.PlanMembresiaRepository;
import GymTrack.example.demo.Service.PlanMembresiaService;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanMembresiaServiceImpl implements PlanMembresiaService
{
    private final PlanMembresiaRepository planMembresiaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlanMembresia> findAll()
    {
        return planMembresiaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanMembresia> findById(Long id)
    {
        return planMembresiaRepository.findById(id);
    }

    @Override
    @Transactional
    public PlanMembresia save(PlanMembresia planMembresia)
    {
        if (planMembresia.getNombre() == null || planMembresia.getNombre().isBlank()
                || planMembresia.getPrecio() == null || planMembresia.getPrecio().compareTo(BigDecimal.ZERO) <= 0
                || planMembresia.getDuracion_meses() == null || planMembresia.getDuracion_meses() <= 0)
        {
            throw new IllegalArgumentException("El plan requiere nombre, precio y duración positivos.");
        }

        return planMembresiaRepository.save(planMembresia);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        planMembresiaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return planMembresiaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanMembresia> findActivos()
    {
        return planMembresiaRepository.findByActivoTrue();
    }
}
