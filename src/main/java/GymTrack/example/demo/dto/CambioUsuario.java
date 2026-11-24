package GymTrack.example.demo.dto;

import lombok.Data;

@Data
public class CambioUsuario
{
    private String username;

    @lombok.ToString.Exclude
    private String password;

    private Boolean activo;
}
