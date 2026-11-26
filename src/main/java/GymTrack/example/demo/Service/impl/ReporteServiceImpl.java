package GymTrack.example.demo.Service.impl;
import GymTrack.example.demo.Repository.ReporteRepository;
import GymTrack.example.demo.Service.ReporteService;
import GymTrack.example.demo.model.Reporte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {
    private final ReporteRepository reporteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Reporte> findAll() { return reporteRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reporte> findById(Integer id) { return reporteRepository.findById(id); }

    @Override
    @Transactional
    public Reporte save(Reporte reporte) { return reporteRepository.save(reporte); }

    @Override
    @Transactional
    public void deleteById(Integer id) { reporteRepository.deleteById(id); }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) { return reporteRepository.existsById(id); }
}
