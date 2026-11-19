package GymTrack.example.demo.controller.login;

import GymTrack.example.demo.dto.Login;
import GymTrack.example.demo.model.Usuario;
import GymTrack.example.demo.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag( name = "Login", description = "Acceso al sistema")
@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class LoginController
{
    private final UsuarioService usuarioService;

    @Operation(summary = "Valida credenciales", description = "valida credenciales del sistema")
    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Login login)
    {

        Usuario usuario = usuarioService.findByLogin( login );
        if( usuario != null )
        {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(usuario);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build( );
    }

}
