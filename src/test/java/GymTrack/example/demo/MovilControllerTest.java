package GymTrack.example.demo.controller;
import GymTrack.example.demo.model.Membresia;
import GymTrack.example.demo.model.PlanMembresia;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MovilControllerTest {
    private final LocalDate hoy = LocalDate.of(2026, 11, 26);
    private Membresia m(int id, String estado, LocalDate inicio, LocalDate fin) {
        return Membresia.builder().id_mensualidad(id).estatus(estado).fecha_i(inicio).fecha_f(fin)
            .precio_cobrado(new BigDecimal("300.00")).plan(PlanMembresia.builder().nombre("Mensual").build()).build();
    }
    @Test void vigenteTienePrioridadSobreFutura() {
        var vigente = m(1,"VIGENTE",hoy.minusDays(10),hoy.plusDays(20));
        var futura = m(2,"VIGENTE",hoy.plusDays(21),hoy.plusDays(51));
        var r=MovilController.calcularMembresia(List.of(vigente,futura),hoy);
        assertEquals("VIGENTE",r.estado()); assertEquals(20,r.diasRestantes());
    }
    @Test void venceHoySigueVigente() {
        var r=MovilController.calcularMembresia(List.of(m(1,"VIGENTE",hoy.minusDays(30),hoy)),hoy);
        assertEquals("VIGENTE",r.estado()); assertEquals(0,r.diasRestantes());
    }
    @Test void fechaPasadaAunqueEstatusNoActualizado() {
        assertEquals("VENCIDA",MovilController.calcularMembresia(List.of(m(1,"VIGENTE",hoy.minusDays(31),hoy.minusDays(1))),hoy).estado());
    }
    @Test void futura() { assertEquals("PENDIENTE",MovilController.calcularMembresia(List.of(m(1,"VIGENTE",hoy.plusDays(1),hoy.plusDays(31))),hoy).estado()); }
    @Test void cancelada() { assertEquals("CANCELADA",MovilController.calcularMembresia(List.of(m(1,"CANCELADA",hoy.minusDays(1),hoy.plusDays(29))),hoy).estado()); }
    @Test void sinMembresia() { assertEquals("SIN_MEMBRESIA",MovilController.calcularMembresia(List.of(),hoy).estado()); }
}
