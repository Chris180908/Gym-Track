package GymTrack.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ACCESO_GYM")
public class AccesoGym
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ACCESO")
    private Long id_acceso;

    @ManyToOne
    @JoinColumn(name = "ID_SOCIO", nullable = false)
    private Socio socio;

    @Column(name = "FECHA_ACCESO", nullable = false)
    private LocalDateTime fecha_acceso;

    @Enumerated(EnumType.STRING)
    @Column(name = "RESULTADO", nullable = false)
    private ResultadoAcceso resultado;
}
