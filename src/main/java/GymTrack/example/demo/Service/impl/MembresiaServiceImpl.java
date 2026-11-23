package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.MembresiaRepository;
import GymTrack.example.demo.Service.MembresiaService;
import GymTrack.example.demo.model.Membresia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembresiaServiceImpl implements MembresiaService {

    @Autowired
    private MembresiaRepository membresiaRepository;

    @Override
    public List<Membresia> findAll() {
        return membresiaRepository.findAll();
    }

    @Override
    public Optional<Membresia> findById(Integer id) {
        return membresiaRepository.findById(id);
    }

    @Override
    public Membresia save(Membresia membresia) {
        return membresiaRepository.save(membresia);
    }

    @Override
    public void deleteById(Integer id) {
        membresiaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return membresiaRepository.existsById(id);
    }

    @Override
    public List<Membresia> findBySocioId(Integer idSocio) {
        return membresiaRepository.findBySocioId(idSocio);
    }
}