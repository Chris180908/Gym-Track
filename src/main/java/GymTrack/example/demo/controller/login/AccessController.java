package GymTrack.example.demo.controller.login;

import GymTrack.example.demo.Service.*;
import GymTrack.example.demo.dto.*;
import GymTrack.example.demo.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class AccessController
{
    private final UsuarioService usuarioService;
    private final SocioService socioService;
    private final RecepcionistaService recepcionistaService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody Login login, HttpServletRequest request)
    {
        Usuario usuario = usuarioService.findByLogin(login);

        if (usuario == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String rol = usuario.getRol().getNombre();

        if ("SOCIO".equals(rol))
        {
            Socio socio = socioService.findById(usuario.getId()).orElse(null);
            if (socio == null || socio.getEstatus() != EstatusSocio.ACTIVO)
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        else if (!"RECEPCIONISTA".equals(rol) || !recepcionistaService.existsById(usuario.getId()))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpSession anterior = request.getSession(false);
        if (anterior != null)
        {
            anterior.invalidate();
        }

        request.getSession(true).setAttribute("idUsuario", usuario.getId());

        LoginResponse respuesta = new LoginResponse(usuario.getId(), usuario.getUsername(), rol);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session)
    {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
