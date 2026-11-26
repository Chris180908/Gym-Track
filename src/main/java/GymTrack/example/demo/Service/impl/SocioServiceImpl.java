package GymTrack.example.demo.Service.impl;
import GymTrack.example.demo.Repository.SocioRepository;
import GymTrack.example.demo.Repository.MembresiaRepository;
import GymTrack.example.demo.Repository.PagoRepository;
import GymTrack.example.demo.Service.SocioService;
import GymTrack.example.demo.model.Socio;
import GymTrack.example.demo.model.Membresia;
import GymTrack.example.demo.model.Pago;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocioServiceImpl implements SocioService {
    private final SocioRepository socioRepository;
    private final MembresiaRepository membresiaRepository;
    private final PagoRepository pagoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Socio> findAll() { return socioRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Optional<Socio> findById(Integer id) { return socioRepository.findById(id); }

    @Override
    @Transactional
    public Socio save(Socio socio) { return socioRepository.save(socio); }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        Socio socio = socioRepository.findById(id).orElse(null);
        if (socio == null) return;
        if (!"INACTIVO".equals(socio.getEstatus())) {
            throw new IllegalStateException("Solo se pueden eliminar socios inactivos.");
        }

        List<Membresia> membresias = membresiaRepository.findBySocioId(id);
        LocalDate hoy = LocalDate.now();
        for (Membresia membresia : membresias) {
            if ("VIGENTE".equals(membresia.getEstatus())
                    && !membresia.getFecha_f().isBefore(hoy)) {
                throw new IllegalStateException("El socio tiene una membresía vigente o programada.");
            }
        }

        for (Pago pago : pagoRepository.findAll()) {
            if (id.equals(pago.getMembresia().getSocio().getId_socio())) {
                pagoRepository.deleteById(pago.getId_pago());
            }
        }
        for (Membresia membresia : membresias) {
            membresiaRepository.deleteById(membresia.getId_mensualidad());
        }
        socioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) { return socioRepository.existsById(id); }
}
