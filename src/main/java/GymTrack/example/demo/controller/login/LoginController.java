package GymTrack.example.demo.controller.login;

import GymTrack.example.demo.Service.RecepcionistaService;
import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.Recepcionista;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final RecepcionistaService recepcionistaService;

    @GetMapping("/index")
    public String mostrarIndex(Model model) {
        model.addAttribute("login", new Login());
        return "index";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @ModelAttribute("login") Login login,
            HttpSession session,
            Model model) {

        if (login.getUsername() == null || login.getUsername().isBlank()
                || login.getPassword() == null || login.getPassword().isBlank()) {
            model.addAttribute("error", "Ingresa tu correo electrónico y contraseña.");
            return "index";
        }

        Recepcionista recepcionista = recepcionistaService.findByLogin(login);

        if (recepcionista == null) {
            model.addAttribute("error", "Correo o contraseña incorrrecta ❗");
            return "index";
        }

        session.setAttribute("recepcionistaLogueado", recepcionista);
        return "redirect:/inicio.jsp";
    }
}