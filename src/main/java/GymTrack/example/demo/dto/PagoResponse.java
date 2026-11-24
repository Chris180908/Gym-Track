package GymTrack.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponse
{
    private Long id;

    private BigDecimal monto;

    private LocalDateTime fechaPago;

    private String metodoPago;

    private String concepto;

    private String origen;

    private String moneda;
}
