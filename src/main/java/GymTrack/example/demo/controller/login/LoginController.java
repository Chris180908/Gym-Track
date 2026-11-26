package GymTrack.example.demo.controller.login;
import GymTrack.example.demo.controller.Acceso;
import GymTrack.example.demo.Service.RecepcionistaService;
import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.Recepcionista;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final RecepcionistaService recepcionistaService;

    @GetMapping({"/", "/index", "/login"})
    public String mostrarLogin(HttpSession session) {
        if (Acceso.sesion(session)) {
            if (Acceso.sesion(session, recepcionistaService)) return "redirect:/inicio";
            return "redirect:/login";
        }
        Acceso.token(session);
        return "index";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute Login login, @RequestParam String token, HttpServletRequest request, HttpSession session, Model model) {
        if (!Acceso.post(session, token)) return "redirect:/login";
        Recepcionista recepcionista = recepcionistaService.findByLogin(login);
        if (recepcionista == null || recepcionista.getRol() == null ||
            !("ADMIN".equals(recepcionista.getRol().getNombre()) || "RECEPCIONISTA".equals(recepcionista.getRol().getNombre()))) {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "index";
        }
        request.changeSessionId();
        session.setAttribute("idRecepcion", recepcionista.getId_recepcion());
        session.setAttribute("rol", recepcionista.getRol().getNombre());
        session.setAttribute("nombre", recepcionista.getNom_recepcion());
        session.removeAttribute("token");
        Acceso.token(session);
        return "redirect:/inicio";
    }

    @PostMapping("/salir")
    public String salir(@RequestParam String token, HttpSession session) {
        if (Acceso.post(session, token)) session.invalidate();
        return "redirect:/login";
    }
}
