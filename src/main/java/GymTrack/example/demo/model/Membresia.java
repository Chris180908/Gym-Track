package GymTrack.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "MEMBRESIA")
public class Membresia
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MENSUALIDAD")
    private Long id_mensualidad;

    @Column(name = "TIPO", length = 50, nullable = false)
    private String tipo;

    @Column(name = "PRECIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "FECHA_I", nullable = false)
    private LocalDate fecha_i;

    @Column(name = "FECHA_F", nullable = false)
    private LocalDate fecha_f;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTATUS", nullable = false)
    private EstatusMembresia estatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "MODALIDAD", nullable = false)
    private Modalidad modalidad;

    @ManyToOne
    @JoinColumn(name = "ID_SOCIO", nullable = false)
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "ID_PLAN", nullable = true)
    private PlanMembresia plan;

    @ManyToOne
    @JoinColumn(name = "ID_SUSCRIPCION", nullable = true)
    private Suscripcion suscripcion;

    @Transient
    public String getEstatusActual()
    {
        if (estatus == EstatusMembresia.CANCELADA)
        {
            return "CANCELADA";
        }

        LocalDate hoy = LocalDate.now();

        if (fecha_f.isBefore(hoy))
        {
            return "VENCIDA";
        }

        if (fecha_i.isAfter(hoy))
        {
            return "PROGRAMADA";
        }

        return estatus.name();
    }
}
