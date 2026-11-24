package GymTrack.example.demo.controller;

import GymTrack.example.demo.Service.*;
import GymTrack.example.demo.model.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class VistaController
{
    private final SesionService sesionService;
    private final SocioService socioService;
    private final MembresiaService membresiaService;
    private final PagoService pagoService;
    private final ReporteService reporteService;
    private final AccesoGymService accesoGymService;

    @GetMapping({"/", "/index", "/login"})
    public String index()
    {
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model)
    {
        if (!tieneAcceso(session))
        {
            return "redirect:/";
        }
        model.addAttribute("socios", socioService.findAll());
        return "inicio";
    }

    @GetMapping("/socios")
    public String socios(HttpSession session, Model model)
    {
        if (!tieneAcceso(session))
        {
            return "redirect:/";
        }
        model.addAttribute("socios", socioService.findAll());
        return "socios";
    }

    @GetMapping("/membresias")
    public String membresias(HttpSession session, Model model)
    {
        if (!tieneAcceso(session))
        {
            return "redirect:/";
        }
        model.addAttribute("membresias", membresiaService.findAll());
        int[] meses = new int[12];
        for (AccesoGym acceso : accesoGymService.findAll())
        {
            if (acceso.getResultado() == ResultadoAcceso.PERMITIDO
                    && acceso.getFecha_acceso().getYear() == LocalDate.now().getYear())
            {
                meses[acceso.getFecha_acceso().getMonthValue() - 1]++;
            }
        }
        model.addAttribute("datosGimnasio", java.util.Arrays.toString(meses));
        return "membresias";
    }

    @GetMapping("/reportes")
    public String reportes(HttpSession session, Model model)
    {
        if (!tieneAcceso(session))
        {
            return "redirect:/";
        }
        model.addAttribute("socios", socioService.findAll());
        model.addAttribute("membresias", membresiaService.findAll());
        model.addAttribute("pagos", pagoService.findAll());
        model.addAttribute("reportes", reporteService.findAll());
        return "reportes";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model)
    {
        if (!tieneAcceso(session))
        {
            return "redirect:/";
        }
        model.addAttribute("recepcionista", sesionService.obtenerRecepcionista(session));
        return "perfil";
    }

    private boolean tieneAcceso(HttpSession session)
    {
        try
        {
            sesionService.obtenerRecepcionista(session);
            return true;
        }
        catch (ResponseStatusException error)
        {
            return false;
        }
    }
}
