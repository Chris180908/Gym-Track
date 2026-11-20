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
@Table(name = "RECEPCIONISTA")
public class Recepcionista
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "ID_RECEPCION")
    private Integer id_recepcion;

    @Column(name = "NOM_RECEPCION")
    private String nom_recepcion;

    @Column(name = "CONTRA_RECEPCION")
    private String contra_recepcion;

    @Column(name = "NUM_CALLE_R")
    private String num_calle_r;

    @Column(name = "COL_R")
    private String col_r;

    @Column(name = "EDO_R")
    private String edo_r;

    @Column(name = "COD_POST_R")
    private String cod_post_r;

    @Column(name = "TEL_RECEPCION")
    private String tel_recepcion;

    @Column(name = "CORREO_RECEPCION")
    private String correo_recepcion;

}
