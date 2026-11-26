package GymTrack.example.demo.Service.impl;
import GymTrack.example.demo.Repository.RolRepository;
import GymTrack.example.demo.Service.RolService;
import GymTrack.example.demo.model.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Rol> findAll() { return rolRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findById(Integer id) { return rolRepository.findById(id); }

    @Override
    @Transactional
    public Rol save(Rol rol) { return rolRepository.save(rol); }

    @Override
    @Transactional
    public void deleteById(Integer id) { rolRepository.deleteById(id); }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) { return rolRepository.existsById(id); }
    @Override
    @Transactional(readOnly = true)
    public Rol findByNombre(String nombre) { return rolRepository.findByNombre(nombre); }
}
