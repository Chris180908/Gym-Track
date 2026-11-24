package GymTrack.example.demo.dto;

import GymTrack.example.demo.model.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompraMembresia
{
    private Long idSocio;

    private Long idPlan;

    private MetodoPago metodoPago;
}
