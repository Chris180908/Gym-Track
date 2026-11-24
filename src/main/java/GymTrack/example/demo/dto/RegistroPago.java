package GymTrack.example.demo.dto;

import GymTrack.example.demo.model.MetodoPago;

import lombok.Data;

@Data
public class RegistroPago
{
    private Long idMembresia;

    private MetodoPago metodoPago;
}
