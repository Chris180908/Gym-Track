package GymTrack.example.demo.Service.impl;
import GymTrack.example.demo.Repository.PlanMembresiaRepository;
import GymTrack.example.demo.Service.PlanMembresiaService;
import GymTrack.example.demo.model.PlanMembresia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanMembresiaServiceImpl implements PlanMembresiaService {
    private final PlanMembresiaRepository planMembresiaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlanMembresia> findAll() { return planMembresiaRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanMembresia> findById(Integer id) { return planMembresiaRepository.findById(id); }

    @Override
    @Transactional
    public PlanMembresia save(PlanMembresia planMembresia) { return planMembresiaRepository.save(planMembresia); }

    @Override
    @Transactional
    public void deleteById(Integer id) { planMembresiaRepository.deleteById(id); }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) { return planMembresiaRepository.existsById(id); }
}
