package GymTrack.example.demo.Service.impl;

import GymTrack.example.demo.Repository.SocioRepository;
import GymTrack.example.demo.Service.SocioService;
import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.Socio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocioServiceImpl implements SocioService
{
    private final SocioRepository socioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Socio> findAll()
    {
        return socioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Socio> findById(Long id)
    {
        return socioRepository.findById(id);
    }

    @Override
    @Transactional
    public Socio save(Socio socio)
    {
        return socioRepository.save(socio);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        socioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return socioRepository.existsById(id);
    }

    @Override
    public Socio findByLogin(Login login)
    {
        Socio socio = socioRepository.findByCorreo_socio( login.getUsername( ) );
        if( socio == null )
        {
            return null;
        }
        if( socio.getContra_socio( ).equals( login.getPassword( ) ) )
        {
            return socio;
        }
        return null;
    }
}