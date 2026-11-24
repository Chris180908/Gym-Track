package GymTrack.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "REPORTE")
public class Reporte
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REPORTE")
    private Long id_reporte;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_REPORTE", nullable = false)
    private TipoReporte tipo_reporte;

    @Column(name = "FECHA_INICIO_FILTRO")
    private LocalDate fecha_inicio_filtro;

    @Column(name = "FECHA_FIN_FILTRO")
    private LocalDate fecha_fin_filtro;

    @Column(name = "FECHA_G", nullable = false)
    private LocalDateTime fecha_g;

    @ManyToOne
    @JoinColumn(name = "ID_RECEPCION", nullable = false)
    private Recepcionista recepcionista;
}
