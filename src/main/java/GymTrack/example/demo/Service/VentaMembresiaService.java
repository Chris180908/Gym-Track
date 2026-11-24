package GymTrack.example.demo.Service;

import GymTrack.example.demo.Repository.MembresiaRepository;
import GymTrack.example.demo.Repository.SocioRepository;
import GymTrack.example.demo.dto.CompraMembresia;
import GymTrack.example.demo.dto.CambioMembresia;
import GymTrack.example.demo.dto.RegistroPago;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VentaMembresiaService
{
    private final SocioRepository socioRepository;
    private final MembresiaRepository membresiaRepository;
    private final PlanMembresiaService planMembresiaService;
    private final MembresiaService membresiaService;
    private final PagoService pagoService;

    // Registra una venta presencial de prueba, no procesa una tarjeta.
    @Transactional
    public Membresia registrarVenta(CompraMembresia datos, Recepcionista recepcionista)
    {
        if (datos.getIdSocio() == null || datos.getIdPlan() == null || datos.getMetodoPago() == null)
        {
            throw new IllegalArgumentException("Selecciona socio, plan y método de pago.");
        }

        Socio socio = socioRepository.findForUpdate(datos.getIdSocio())
                .orElseThrow(() -> new IllegalArgumentException("Socio inexistente."));
        PlanMembresia plan = planMembresiaService.findById(datos.getIdPlan())
                .orElseThrow(() -> new IllegalArgumentException("Plan inexistente."));

        if (socio.getEstatus() != EstatusSocio.ACTIVO || !plan.isActivo())
        {
            throw new IllegalArgumentException("El socio o el plan están inactivos.");
        }

        LocalDate inicio = LocalDate.now();

        // FECHA_F es inclusiva. El siguiente periodo empieza un día después.
        for (Membresia anterior : membresiaService.findBySocioId(socio.getId_socio()))
        {
            if (anterior.getEstatus() != EstatusMembresia.CANCELADA && !anterior.getFecha_f().isBefore(inicio))
            {
                inicio = anterior.getFecha_f().plusDays(1);
            }
        }

        Membresia membresia = Membresia.builder()
                .tipo(plan.getNombre())
                .precio(plan.getPrecio())
                .fecha_i(inicio)
                .fecha_f(inicio.plusMonths(plan.getDuracion_meses()).minusDays(1))
                .estatus(EstatusMembresia.VIGENTE)
                .modalidad(Modalidad.PREPAGO)
                .socio(socio)
                .plan(plan)
                .build();

        membresia = membresiaService.save(membresia);

        Pago pago = Pago.builder()
                .monto(plan.getPrecio())
                .fecha_pago(LocalDateTime.now())
                .metodo_pago(datos.getMetodoPago())
                .concepto("Membresía " + plan.getNombre())
                .origen(OrigenPago.RECEPCION)
                .moneda("MXN")
                .membresia(membresia)
                .recepcionista(recepcionista)
                .build();

        pagoService.save(pago);

        return membresia;
    }

    @Transactional
    public Membresia actualizarMembresia(Long id, CambioMembresia datos)
    {
        Membresia membresia = membresiaRepository.findForUpdate(id).orElseThrow();
        Long idSocio = membresia.getSocio().getId_socio();
        socioRepository.findForUpdate(idSocio).orElseThrow();

        if (membresia.getModalidad() == Modalidad.SUSCRIPCION)
        {
            throw new IllegalArgumentException("Los periodos de suscripción no se editan manualmente.");
        }

        if (datos.getFechaInicio() == null || datos.getFechaFin() == null
                || datos.getFechaFin().isBefore(datos.getFechaInicio()) || datos.getEstatus() == null)
        {
            throw new IllegalArgumentException("Indica las fechas y el estado de la membresía.");
        }

        if (datos.getEstatus() != EstatusMembresia.CANCELADA)
        {
            for (Membresia anterior : membresiaService.findBySocioId(idSocio))
            {
                if (!anterior.getId_mensualidad().equals(id)
                        && anterior.getEstatus() != EstatusMembresia.CANCELADA
                        && !anterior.getFecha_f().isBefore(datos.getFechaInicio())
                        && !anterior.getFecha_i().isAfter(datos.getFechaFin()))
                {
                    throw new IllegalArgumentException("Las fechas coinciden con otra membresía del socio.");
                }
            }
        }

        membresia.setFecha_i(datos.getFechaInicio());
        membresia.setFecha_f(datos.getFechaFin());
        membresia.setEstatus(datos.getEstatus());

        return membresiaService.save(membresia);
    }

    @Transactional
    public void cancelarMembresia(Long id)
    {
        Membresia membresia = membresiaRepository.findForUpdate(id).orElseThrow();
        socioRepository.findForUpdate(membresia.getSocio().getId_socio()).orElseThrow();
        membresia.setEstatus(EstatusMembresia.CANCELADA);
        membresiaService.save(membresia);
    }

    // Registro presencial de prueba para una membresía que aún no tiene pago.
    @Transactional
    public Pago registrarPagoPendiente(RegistroPago datos, Recepcionista recepcionista)
    {
        if (datos.getIdMembresia() == null || datos.getMetodoPago() == null)
        {
            throw new IllegalArgumentException("Selecciona la membresía y el método de pago.");
        }

        Membresia membresia = membresiaRepository.findForUpdate(datos.getIdMembresia())
                .orElseThrow(() -> new IllegalArgumentException("Membresía inexistente."));
        Long idSocio = membresia.getSocio().getId_socio();
        socioRepository.findForUpdate(idSocio).orElseThrow();

        if (membresia.getModalidad() != Modalidad.PREPAGO
                || membresia.getEstatus() == EstatusMembresia.CANCELADA)
        {
            throw new IllegalArgumentException("No se puede registrar un pago manual para esta membresía.");
        }

        if (pagoService.existePagoDeMembresia(datos.getIdMembresia()))
        {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "La membresía ya tiene un pago.");
        }

        Pago pago = Pago.builder()
                .monto(membresia.getPrecio())
                .fecha_pago(LocalDateTime.now())
                .metodo_pago(datos.getMetodoPago())
                .concepto("Membresía " + membresia.getTipo())
                .origen(OrigenPago.RECEPCION)
                .moneda("MXN")
                .membresia(membresia)
                .recepcionista(recepcionista)
                .build();

        return pagoService.save(pago);
    }
}
