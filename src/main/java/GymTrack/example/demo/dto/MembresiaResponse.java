package GymTrack.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembresiaResponse
{
    private Long id;

    private String tipo;

    private BigDecimal precio;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String estatus;

    private String modalidad;
}
