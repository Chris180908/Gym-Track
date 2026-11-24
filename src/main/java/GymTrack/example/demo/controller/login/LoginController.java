package GymTrack.example.demo.controller.login;

import GymTrack.example.demo.Service.RecepcionistaService;
import GymTrack.example.demo.Service.UsuarioService;
import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginController
{
    private final UsuarioService usuarioService;
    private final RecepcionistaService recepcionistaService;

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute Login login, HttpServletRequest request, Model model)
    {
        Usuario usuario = usuarioService.findByLogin(login);

        if (usuario == null || !"RECEPCIONISTA".equals(usuario.getRol().getNombre())
                || !recepcionistaService.existsById(usuario.getId()))
        {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "index";
        }

        HttpSession anterior = request.getSession(false);
        if (anterior != null)
        {
            anterior.invalidate();
        }

        request.getSession(true).setAttribute("idUsuario", usuario.getId());
        return "redirect:/inicio";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }
}
