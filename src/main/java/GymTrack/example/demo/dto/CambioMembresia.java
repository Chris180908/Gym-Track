package GymTrack.example.demo.dto;

import GymTrack.example.demo.model.EstatusMembresia;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CambioMembresia
{
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private EstatusMembresia estatus;
}
