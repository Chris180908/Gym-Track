package GymTrack.example.demo.dto;

import GymTrack.example.demo.model.TipoReporte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistroReporte
{
    private TipoReporte tipo;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
