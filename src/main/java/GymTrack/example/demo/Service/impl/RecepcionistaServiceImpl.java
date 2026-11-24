package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.RecepcionistaRepository;
import GymTrack.example.demo.Service.RecepcionistaService;
import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.Recepcionista;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecepcionistaServiceImpl implements RecepcionistaService
{
    private final RecepcionistaRepository recepcionistaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Recepcionista> findAll()
    {
        return recepcionistaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Recepcionista> findById(Long id)
    {
        return recepcionistaRepository.findById(id);
    }

    @Override
    @Transactional
    public Recepcionista save(Recepcionista recepcionista)
    {
        return recepcionistaRepository.save(recepcionista);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        recepcionistaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return recepcionistaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Recepcionista findByLogin(Login login)
    {
        if (login == null || login.getUsername() == null || login.getPassword() == null)
        {
            return null;
        }

        String correo = login.getUsername().trim();
        String password = login.getPassword().trim();

        Recepcionista recepcionista = recepcionistaRepository.findByCorreo_recepcion(correo);
        if (recepcionista == null)
        {
            return null;
        }

        if (recepcionista.getContra_recepcion() != null && recepcionista.getContra_recepcion().equals(password))
        {
            return recepcionista;
        }

        return null;
    }
}