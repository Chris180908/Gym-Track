package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.ReporteRepository;
import GymTrack.example.demo.Service.ReporteService;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService
{
    private final ReporteRepository reporteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Reporte> findAll()
    {
        return reporteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reporte> findById(Long id)
    {
        return reporteRepository.findById(id);
    }

    @Override
    @Transactional
    public Reporte save(Reporte reporte)
    {
        if (reporte.getTipo_reporte() == null || reporte.getRecepcionista() == null)
        {
            throw new IllegalArgumentException("Faltan datos del reporte.");
        }

        if (reporte.getFecha_inicio_filtro() != null && reporte.getFecha_fin_filtro() != null
                && reporte.getFecha_fin_filtro().isBefore(reporte.getFecha_inicio_filtro()))
        {
            throw new IllegalArgumentException("El intervalo de fechas es incorrecto.");
        }

        if (reporte.getFecha_g() == null)
        {
            reporte.setFecha_g(LocalDateTime.now());
        }

        return reporteRepository.save(reporte);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        reporteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return reporteRepository.existsById(id);
    }
}
