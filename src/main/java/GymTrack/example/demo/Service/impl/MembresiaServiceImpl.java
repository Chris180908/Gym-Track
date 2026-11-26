package GymTrack.example.demo.Service.impl;
import GymTrack.example.demo.Repository.MembresiaRepository;
import GymTrack.example.demo.Service.MembresiaService;
import GymTrack.example.demo.model.Membresia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembresiaServiceImpl implements MembresiaService {
    private final MembresiaRepository membresiaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Membresia> findAll() { return membresiaRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Optional<Membresia> findById(Integer id) { return membresiaRepository.findById(id); }

    @Override
    @Transactional
    public Membresia save(Membresia membresia) { return membresiaRepository.save(membresia); }

    @Override
    @Transactional
    public void deleteById(Integer id) { membresiaRepository.deleteById(id); }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) { return membresiaRepository.existsById(id); }
    @Override
    @Transactional(readOnly = true)
    public List<Membresia> findBySocioId(Integer idSocio) { return membresiaRepository.findBySocioId(idSocio); }
}
