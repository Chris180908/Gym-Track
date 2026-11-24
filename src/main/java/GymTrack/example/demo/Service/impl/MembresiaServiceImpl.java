package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.MembresiaRepository;
import GymTrack.example.demo.Service.MembresiaService;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembresiaServiceImpl implements MembresiaService
{
    private final MembresiaRepository membresiaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Membresia> findAll()
    {
        return membresiaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Membresia> findById(Long id)
    {
        return membresiaRepository.findById(id);
    }

    @Override
    @Transactional
    public Membresia save(Membresia membresia)
    {
        if (membresia.getPrecio() == null || membresia.getPrecio().compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }

        if (membresia.getFecha_i() == null || membresia.getFecha_f() == null
                || membresia.getFecha_f().isBefore(membresia.getFecha_i()))
        {
            throw new IllegalArgumentException("Las fechas de la membresía son incorrectas.");
        }

        if (membresia.getSocio() == null || membresia.getModalidad() == null || membresia.getEstatus() == null)
        {
            throw new IllegalArgumentException("Faltan datos de la membresía.");
        }

        if (membresia.getModalidad() == Modalidad.SUSCRIPCION)
        {
            if (membresia.getSuscripcion() == null || membresia.getPlan() == null
                    || !membresia.getSocio().getId_socio().equals(membresia.getSuscripcion().getSocio().getId_socio())
                    || !membresia.getPlan().getId_plan().equals(membresia.getSuscripcion().getPlan().getId_plan()))
            {
                throw new IllegalArgumentException("La suscripción no corresponde al socio y plan.");
            }
        }
        else if (membresia.getSuscripcion() != null)
        {
            throw new IllegalArgumentException("Una membresía de prepago no tiene suscripción.");
        }

        return membresiaRepository.save(membresia);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        membresiaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return membresiaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Membresia> findBySocioId(Long idSocio)
    {
        return membresiaRepository.findBySocioId(idSocio);
    }
}
