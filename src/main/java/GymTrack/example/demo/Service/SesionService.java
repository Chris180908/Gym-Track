package GymTrack.example.demo.Service;

import GymTrack.example.demo.model.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SesionService
{
    private final UsuarioService usuarioService;
    private final SocioService socioService;
    private final RecepcionistaService recepcionistaService;

    public Usuario obtenerUsuario(HttpSession session, String rol)
    {
        Object id = session.getAttribute("idUsuario");

        if (!(id instanceof Long))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Inicia sesión.");
        }

        Usuario usuario = usuarioService.findById((Long) id).orElse(null);

        if (usuario == null || !usuario.isActivo())
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cuenta no disponible.");
        }

        if (!rol.equals(usuario.getRol().getNombre()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso.");
        }

        return usuario;
    }

    public Socio obtenerSocio(HttpSession session)
    {
        Usuario usuario = obtenerUsuario(session, "SOCIO");
        Socio socio = socioService.findById(usuario.getId()).orElse(null);

        if (socio == null || socio.getEstatus() != EstatusSocio.ACTIVO)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Socio no disponible.");
        }

        return socio;
    }

    public Recepcionista obtenerRecepcionista(HttpSession session)
    {
        Usuario usuario = obtenerUsuario(session, "RECEPCIONISTA");

        return recepcionistaService.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Perfil no disponible."));
    }
}
