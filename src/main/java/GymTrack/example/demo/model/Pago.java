package GymTrack.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PAGO")
public class Pago {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "ID_PAGO")
    private Integer id_pago;

    @Column(name = "FECHA_PAGO")
    private LocalDateTime fecha_pago;

    @Column(name = "METODO_PAGO")
    private String metodo_pago;

    @Column(name = "CONCEPTO")
    private String concepto;

    @ManyToOne
    @JoinColumn(name = "ID_MENSUALIDAD", nullable = false)
    private Membresia membresia;

    @ManyToOne
    @JoinColumn(name = "ID_RECEPCION", nullable = false)
    private Recepcionista recepcionista;

    @Column(name = "MONTO", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @PrePersist
    protected void onCreate() {
        if (this.fecha_pago == null) {
            this.fecha_pago = LocalDateTime.now();
        }
    }
}
