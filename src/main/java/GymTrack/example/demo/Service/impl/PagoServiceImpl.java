package GymTrack.example.demo.Service.impl;
import GymTrack.example.demo.Repository.PagoRepository;
import GymTrack.example.demo.Service.PagoService;
import GymTrack.example.demo.model.Pago;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {
    private final PagoRepository pagoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findAll() { return pagoRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> findById(Integer id) { return pagoRepository.findById(id); }

    @Override
    @Transactional
    public Pago save(Pago pago) { return pagoRepository.save(pago); }

    @Override
    @Transactional
    public void deleteById(Integer id) { pagoRepository.deleteById(id); }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) { return pagoRepository.existsById(id); }
}
