package GymTrack.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping("/")
    public String index() {
        return "index"; // Abre /index.jsp
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Abre /login.jsp
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "inicio"; // Abre /inicio.jsp
    }

    @GetMapping("/socios")
    public String socios() {
        return "socios"; // Abre /socios.jsp
    }

    @GetMapping("/membresias")
    public String membresias() {
        return "membresias"; // Abre /membresias.jsp
    }

    @GetMapping("/reportes")
    public String reportes() {
        return "reportes"; // Abre /reportes.jsp
    }
}