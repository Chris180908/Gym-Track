package GymTrack.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistroSocio
{
    private String username;

    @lombok.ToString.Exclude
    private String password;

    private String nombre;

    private String telefono;

    private String correo;

    private String numCalle;

    private String colonia;

    private String estado;

    private String codigoPostal;
}
