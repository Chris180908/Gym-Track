package GymTrack.example.demo.Service;

import GymTrack.example.demo.Repository.RolRepository;
import GymTrack.example.demo.dto.CambioUsuario;
import GymTrack.example.demo.dto.DatosSocio;
import GymTrack.example.demo.dto.RegistroUsuario;
import GymTrack.example.demo.dto.RegistroSocio;
import GymTrack.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RegistroService
{
    private final UsuarioService usuarioService;
    private final SocioService socioService;
    private final RecepcionistaService recepcionistaService;
    private final RolRepository rolRepository;

    @Transactional
    public Socio registrarSocio(RegistroSocio datos, Recepcionista recepcionista)
    {
        if (datos.getNombre() == null || datos.getNombre().isBlank())
        {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        Usuario usuario = Usuario.builder()
                .username(datos.getUsername())
                .password(datos.getPassword())
                .activo(true)
                .rol(rolRepository.findByNombre("SOCIO"))
                .build();

        usuario = usuarioService.save(usuario);

        Socio socio = Socio.builder()
                .id_socio(usuario.getId())
                .nom_socio(datos.getNombre())
                .correo_socio(datos.getCorreo())
                .tel_socio(datos.getTelefono())
                .num_calle_s(datos.getNumCalle())
                .col_s(datos.getColonia())
                .edo_s(datos.getEstado())
                .cod_post_s(datos.getCodigoPostal())
                .estatus(EstatusSocio.ACTIVO)
                .fecha_registro(LocalDate.now())
                .recepcionista(recepcionista)
                .build();

        return socioService.save(socio);
    }

    @Transactional
    public Socio actualizarSocio(Socio socio, DatosSocio datos)
    {
        socio.setNom_socio(datos.getNombre());
        socio.setTel_socio(datos.getTelefono());
        socio.setCorreo_socio(datos.getCorreo());
        socio.setNum_calle_s(datos.getNumCalle());
        socio.setCol_s(datos.getColonia());
        socio.setEdo_s(datos.getEstado());
        socio.setCod_post_s(datos.getCodigoPostal());

        return socioService.save(socio);
    }

    @Transactional
    public void desactivarSocio(Socio socio)
    {
        socio.setEstatus(EstatusSocio.INACTIVO);
        socioService.save(socio);

        Usuario usuario = usuarioService.findById(socio.getId_socio()).orElseThrow();
        usuario.setActivo(false);
        usuarioService.save(usuario);
    }

    @Transactional
    public Recepcionista registrarRecepcionista(RegistroSocio datos)
    {
        if (datos == null || datos.getNombre() == null || datos.getNombre().isBlank())
        {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        Usuario usuario = Usuario.builder()
                .username(datos.getUsername())
                .password(datos.getPassword())
                .activo(true)
                .rol(rolRepository.findByNombre("RECEPCIONISTA"))
                .build();

        usuario = usuarioService.save(usuario);

        Recepcionista recepcionista = Recepcionista.builder()
                .id_recepcion(usuario.getId())
                .nom_recepcion(datos.getNombre())
                .correo_recepcion(datos.getCorreo())
                .tel_recepcion(datos.getTelefono())
                .num_calle_r(datos.getNumCalle())
                .col_r(datos.getColonia())
                .edo_r(datos.getEstado())
                .cod_post_r(datos.getCodigoPostal())
                .build();

        return recepcionistaService.save(recepcionista);
    }

    @Transactional
    public Recepcionista actualizarRecepcionista(Recepcionista recepcionista, DatosSocio datos)
    {
        recepcionista.setNom_recepcion(datos.getNombre());
        recepcionista.setTel_recepcion(datos.getTelefono());
        recepcionista.setCorreo_recepcion(datos.getCorreo());
        recepcionista.setNum_calle_r(datos.getNumCalle());
        recepcionista.setCol_r(datos.getColonia());
        recepcionista.setEdo_r(datos.getEstado());
        recepcionista.setCod_post_r(datos.getCodigoPostal());

        return recepcionistaService.save(recepcionista);
    }

    @Transactional
    public Usuario registrarUsuario(RegistroUsuario registro, Recepcionista responsable)
    {
        if (registro.getDatos() == null)
        {
            throw new IllegalArgumentException("Los datos de la cuenta son obligatorios.");
        }

        Long id;

        if ("SOCIO".equals(registro.getRol()))
        {
            id = registrarSocio(registro.getDatos(), responsable).getId_socio();
        }
        else if ("RECEPCIONISTA".equals(registro.getRol()))
        {
            id = registrarRecepcionista(registro.getDatos()).getId_recepcion();
        }
        else
        {
            throw new IllegalArgumentException("El rol no corresponde a un perfil disponible.");
        }

        return usuarioService.findById(id).orElseThrow();
    }

    @Transactional
    public Usuario actualizarUsuario(Usuario usuario, CambioUsuario datos, Long idResponsable)
    {
        if (Boolean.FALSE.equals(datos.getActivo()) && usuario.getId().equals(idResponsable))
        {
            throw new IllegalArgumentException("No puedes desactivar tu propia cuenta.");
        }

        if (datos.getActivo() != null)
        {
            usuario.setActivo(datos.getActivo());

            if ("SOCIO".equals(usuario.getRol().getNombre()))
            {
                Socio socio = socioService.findById(usuario.getId()).orElseThrow();
                socio.setEstatus(usuario.isActivo() ? EstatusSocio.ACTIVO : EstatusSocio.INACTIVO);
                socioService.save(socio);
            }
        }

        return usuarioService.actualizarCredenciales(usuario, datos.getUsername(), datos.getPassword());
    }

    @Transactional
    public void desactivarUsuario(Usuario usuario, Long idResponsable)
    {
        CambioUsuario datos = new CambioUsuario();
        datos.setActivo(false);
        actualizarUsuario(usuario, datos, idResponsable);
    }
}
