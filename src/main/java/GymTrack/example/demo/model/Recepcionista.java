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
    // El ID se toma de USUARIO; aquí no se genera otro identificador.
    @Id
    @Column(name = "ID_RECEPCION")
    private Long id_recepcion;

    @Column(name = "NOM_RECEPCION", length = 100, nullable = false)
    private String nom_recepcion;

    @Column(name = "NUM_CALLE_R", length = 20)
    private String num_calle_r;

    @Column(name = "COL_R", length = 100)
    private String col_r;

    @Column(name = "EDO_R", length = 100)
    private String edo_r;

    @Column(name = "COD_POST_R", length = 10)
    private String cod_post_r;

    @Column(name = "TEL_RECEPCION", length = 15)
    private String tel_recepcion;

    @Column(name = "CORREO_RECEPCION", length = 100, unique = true)
    private String correo_recepcion;
}
