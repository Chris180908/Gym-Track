package GymTrack.example.demo.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "MEMBRESIA")
public class Membresia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MENSUALIDAD")
    private Integer id_mensualidad;
    @Column(name = "PRECIO_COBRADO", precision = 10, scale = 2)
    private BigDecimal precio_cobrado;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_I")
    private LocalDate fecha_i;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_F")
    private LocalDate fecha_f;

    @Column(name = "ESTATUS", columnDefinition = "ENUM('VIGENTE','VENCIDA','CANCELADA')")
    private String estatus;

    @ManyToOne
    @JoinColumn(name = "ID_SOCIO", nullable = false)
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "ID_PLAN", nullable = false)
    private PlanMembresia plan;

}
