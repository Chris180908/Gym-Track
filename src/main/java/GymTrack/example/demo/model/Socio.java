package GymTrack.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SOCIO")
public class Socio
{
    // El ID se toma de USUARIO; aquí no se genera otro identificador.
    @Id
    @Column(name = "ID_SOCIO")
    private Long id_socio;

    @Column(name = "NOM_SOCIO", length = 100, nullable = false)
    private String nom_socio;

    @Column(name = "NUM_CALLE_S", length = 20)
    private String num_calle_s;

    @Column(name = "COL_S", length = 100)
    private String col_s;

    @Column(name = "EDO_S", length = 100)
    private String edo_s;

    @Column(name = "COD_POST_S", length = 10)
    private String cod_post_s;

    @Column(name = "TEL_SOCIO", length = 15)
    private String tel_socio;

    @Column(name = "CORREO_SOCIO", length = 100, unique = true)
    private String correo_socio;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTATUS", nullable = false)
    private EstatusSocio estatus;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDate fecha_registro;

    @ManyToOne
    @JoinColumn(name = "ID_RECEPCION", nullable = false)
    private Recepcionista recepcionista;
}
