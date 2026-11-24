package GymTrack.example.demo.Service;

import GymTrack.example.demo.model.Pago;

import java.util.List;
import java.util.Optional;

public interface PagoService
{
    List<Pago> findAll();

    Optional<Pago> findById(Long id);

    Pago save(Pago pago);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existePagoDeMembresia(Long idMembresia);

    List<Pago> findBySocioId(Long idSocio);
}
