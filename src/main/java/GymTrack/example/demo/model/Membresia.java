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
public class Membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MENSUALIDAD")
    private Integer id_mensualidad;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "PRECIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "FECHA_I", nullable = false)
    private LocalDate fecha_i;

    @Column(name = "FECHA_F", nullable = false)
    private LocalDate fecha_f;

    @Column(name = "ESTATUS")
    private String estatus;

    @ManyToOne
    @JoinColumn(name = "ID_SOCIO", nullable = false)
    private Socio socio;

    @PrePersist
    protected void onCreate() {
        if (this.tipo == null || this.tipo.isBlank()) {
            this.tipo = "Mensual";
        }
        if (this.estatus == null || this.estatus.isBlank()) {
            this.estatus = "VIGENTE";
        }
    }
}

