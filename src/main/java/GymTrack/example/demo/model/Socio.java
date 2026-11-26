package GymTrack.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SOCIO")
public class Socio
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "ID_SOCIO")
    private Integer id_socio;

    @Column(name = "NOM_SOCIO")
    private String nom_socio;

    @Column(name = "CONTRA_SOCIO")
    @lombok.ToString.Exclude
    private String contra_socio;

    @Column(name = "NUM_CALLE_S")
    private String num_calle_s;

    @Column(name = "COL_S")
    private String col_s;

    @Column(name = "EDO_S")
    private String edo_s;

    @Column(name = "COD_POST_S")
    private String cod_post_s;

    @Column(name = "TEL_SOCIO")
    private String tel_socio;

    @Column(name = "CORREO_SOCIO")
    private String correo_socio;

    @Column(name = "ESTATUS", columnDefinition = "ENUM('ACTIVO','INACTIVO')")
    private String estatus;

    @ManyToOne
    @JoinColumn(name = "ID_RECEPCION", nullable = false)
    private Recepcionista recepcionista;
    @ManyToOne
    @JoinColumn(name = "ID_ROL", nullable = false)
    private Rol rol;
}
