package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.RolRepository;
import GymTrack.example.demo.Service.RolService;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService
{
    private final RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Rol> findAll()
    {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findById(Long id)
    {
        return rolRepository.findById(id);
    }

    @Override
    @Transactional
    public Rol save(Rol rol)
    {
        if (rol.getNombre() == null || rol.getNombre().isBlank())
        {
            throw new IllegalArgumentException("El nombre del rol es obligatorio.");
        }

        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        rolRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return rolRepository.existsById(id);
    }
}
