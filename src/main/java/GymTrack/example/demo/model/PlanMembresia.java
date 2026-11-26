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
@Table(name = "PLAN_MEMBRESIA")
public class PlanMembresia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PLAN")
    private Integer id_plan;
    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DURACION_DIAS")
    private Integer duracion_dias;

    @Column(name = "PRECIO", precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "ACTIVO")
    private Boolean activo;

}
