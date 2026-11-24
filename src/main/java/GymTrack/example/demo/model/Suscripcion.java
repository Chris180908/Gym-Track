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
@Table(name = "SUSCRIPCION")
public class Suscripcion
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUSCRIPCION")
    private Long id_suscripcion;

    @ManyToOne
    @JoinColumn(name = "ID_SOCIO", nullable = false)
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "ID_PLAN", nullable = false)
    private PlanMembresia plan;

    @Column(name = "REFERENCIA_EXTERNA", length = 150, unique = true)
    private String referencia_externa;

    @Column(name = "PROVEEDOR", length = 50)
    private String proveedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTATUS", nullable = false)
    private EstatusSuscripcion estatus;

    @Column(name = "FECHA_INICIO")
    private LocalDateTime fecha_inicio;

    @Column(name = "PROXIMO_COBRO")
    private LocalDateTime proximo_cobro;
}
