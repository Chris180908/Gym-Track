package GymTrack.example.demo.controller;
import GymTrack.example.demo.Service.RolService;
import GymTrack.example.demo.Service.RecepcionistaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RolController {
    private final RecepcionistaService recepcionistaService;
    private final RolService rolService;
    @GetMapping("/roles")
    public String findAll(HttpSession session, Model model) {
        if (!Acceso.sesion(session, recepcionistaService)) return "redirect:/login";
        if (!Acceso.admin(session)) return "redirect:/inicio";
        model.addAttribute("lista", rolService.findAll());
        return "roles";
    }
}
