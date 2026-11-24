package GymTrack.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocioResponse
{
    private Long id;

    private String nombre;

    private String telefono;

    private String correo;

    private String numCalle;

    private String colonia;

    private String estado;

    private String codigoPostal;

    private String estatus;

    private LocalDate fechaRegistro;
}
