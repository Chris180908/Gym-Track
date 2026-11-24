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
public class Pago
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PAGO")
    private Long id_pago;

    @Column(name = "MONTO", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "FECHA_PAGO", nullable = false)
    private LocalDateTime fecha_pago;

    @Enumerated(EnumType.STRING)
    @Column(name = "METODO_PAGO", nullable = false)
    private MetodoPago metodo_pago;

    @Column(name = "CONCEPTO", length = 100, nullable = false)
    private String concepto;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORIGEN", nullable = false)
    private OrigenPago origen;

    @Column(name = "REFERENCIA_EXTERNA", length = 150, unique = true)
    private String referencia_externa;

    @Column(name = "PROVEEDOR", length = 50)
    private String proveedor;

    @Column(name = "MONEDA", length = 3, nullable = false)
    private String moneda;

    @ManyToOne
    @JoinColumn(name = "ID_MENSUALIDAD", nullable = false)
    private Membresia membresia;

    @ManyToOne
    @JoinColumn(name = "ID_RECEPCION", nullable = true)
    private Recepcionista recepcionista;
}
