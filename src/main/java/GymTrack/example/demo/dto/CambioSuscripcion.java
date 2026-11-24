package GymTrack.example.demo.dto;

import GymTrack.example.demo.model.EstatusSuscripcion;

import lombok.Data;

@Data
public class CambioSuscripcion
{
    private Long idPlan;

    private EstatusSuscripcion estatus;
}
