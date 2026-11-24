package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.PagoRepository;
import GymTrack.example.demo.Service.PagoService;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService
{
    private final PagoRepository pagoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findAll()
    {
        return pagoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> findById(Long id)
    {
        return pagoRepository.findById(id);
    }

    @Override
    @Transactional
    public Pago save(Pago pago)
    {
        if (pago.getMonto() == null || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }

        if (pago.getMembresia() == null || pago.getMetodo_pago() == null || pago.getOrigen() == null)
        {
            throw new IllegalArgumentException("Faltan datos del pago.");
        }

        if (pago.getOrigen() == OrigenPago.RECEPCION && pago.getRecepcionista() == null)
        {
            throw new IllegalArgumentException("El pago presencial requiere un recepcionista.");
        }

        if (pago.getOrigen() == OrigenPago.APP)
        {
            throw new IllegalArgumentException("Los pagos móviles aún no están habilitados.");
        }

        if (pago.getFecha_pago() == null)
        {
            pago.setFecha_pago(LocalDateTime.now());
        }

        return pagoRepository.save(pago);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        pagoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return pagoRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findBySocioId(Long idSocio)
    {
        return pagoRepository.findBySocioId(idSocio);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePagoDeMembresia(Long idMembresia)
    {
        return pagoRepository.contarPorMembresia(idMembresia) > 0;
    }
}
