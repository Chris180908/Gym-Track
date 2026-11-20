package GymTrack.example.demo.Service;


import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.Recepcionista;

import java.util.List;
import java.util.Optional;

public interface RecepcionistaService
{
    List<Recepcionista> findAll();
    Optional<Recepcionista> findById(Long id);
    Recepcionista save(Recepcionista recepcionista);
    void deleteById(Long id);
    boolean existsById(Long id);
    Recepcionista findByLogin(Login login);
}
