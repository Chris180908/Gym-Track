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
@Table(name = "REPORTE")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REPORTE")
    private Integer id_reporte;
    @Column(name = "TIPO_REPORTE", columnDefinition = "ENUM('GENERAL','SOCIOS','MEMBRESIAS','PAGOS')")
    private String tipo_reporte;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_INICIO_FILTRO")
    private LocalDate fecha_inicio_filtro;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FECHA_FIN_FILTRO")
    private LocalDate fecha_fin_filtro;

    @Column(name = "FECHA_G")
    private LocalDateTime fecha_g;

    @ManyToOne
    @JoinColumn(name = "ID_RECEPCION", nullable = false)
    private Recepcionista recepcionista;

}
