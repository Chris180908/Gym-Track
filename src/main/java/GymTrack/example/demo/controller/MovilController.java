package GymTrack.example.demo.controller;

import GymTrack.example.demo.Repository.MembresiaRepository;
import GymTrack.example.demo.Repository.PlanMembresiaRepository;
import GymTrack.example.demo.Repository.SocioRepository;
import GymTrack.example.demo.model.Membresia;
import GymTrack.example.demo.model.Socio;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** API exclusiva de socios. No devuelve entidades JPA ni contraseñas. */
@RestController
@RequestMapping("/api/movil")
public class MovilController {
    private final SocioRepository socios;
    private final MembresiaRepository membresias;
    private final PlanMembresiaRepository planes;
    private final SecureRandom random = new SecureRandom();
    private final Map<String, Sesion> sesiones = new ConcurrentHashMap<>();
    private static final ZoneId ZONA = ZoneId.of("America/Mexico_City");
    private record Sesion(Integer socioId, Instant expira) {}
    public record Login(String correo, String contrasena, boolean recordar) {}
    public record Token(String token) {}
    public record Perfil(Integer id, String nombre, String correo, String telefono,
                         String estatus, String primeraMembresia) {}
    public record Edicion(String nombre, String correo, String telefono) {}
    public record EstadoMembresia(String estado, String plan, String inicio, String fin,
                                  BigDecimal precioCobrado, long diasRestantes) {}
    public record Plan(Integer id, String nombre, Integer duracionDias, BigDecimal precio) {}

    public MovilController(SocioRepository socios, MembresiaRepository membresias,
                           PlanMembresiaRepository planes) {
        this.socios = socios; this.membresias = membresias; this.planes = planes;
    }

    @ModelAttribute
    public void sinCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
    }

    @PostMapping("/login")
    public Token login(@RequestBody Login dato) {
        if (dato.correo() == null || dato.contrasena() == null || dato.correo().length() > 100
                || dato.contrasena().length() > 255) throw error(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos.");
        Socio socio = socios.buscarPorCorreoMovil(dato.correo().trim()).orElse(null);
        // Compatibilidad con el sistema existente: las contraseñas se guardan en texto plano.
        if (!esSocio(socio) || socio.getContra_socio() == null || !MessageDigest.isEqual(
                socio.getContra_socio().getBytes(StandardCharsets.UTF_8),
                dato.contrasena().getBytes(StandardCharsets.UTF_8))) {
            throw error(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos.");
        }
        Instant ahora = Instant.now();
        sesiones.entrySet().removeIf(e -> !e.getValue().expira().isAfter(ahora));
        if (sesiones.size() >= 10000) throw error(HttpStatus.SERVICE_UNAVAILABLE, "Intenta más tarde.");
        byte[] bytes = new byte[32]; random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        sesiones.put(token, new Sesion(socio.getId_socio(), ahora.plus(dato.recordar() ? 7 : 1, ChronoUnit.DAYS)));
        return new Token(token);
    }

    @GetMapping("/perfil")
    public Perfil perfil(@RequestHeader(value="Authorization", required=false) String auth) {
        return perfilDe(socioDe(auth));
    }

    @PutMapping("/perfil")
    public Perfil editar(@RequestHeader(value="Authorization", required=false) String auth, @RequestBody Edicion dato) {
        Socio socio = socioDe(auth);
        String nombre = limpio(dato.nombre()), correo = limpio(dato.correo()), tel = limpio(dato.telefono());
        if (nombre.isEmpty() || nombre.length() > 100 || correo.length() > 100 ||
                !correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+") || tel.length() > 15 ||
                (!tel.isEmpty() && !tel.matches("[+0-9 ()-]+")))
            throw error(HttpStatus.BAD_REQUEST, "Revisa el nombre, correo y teléfono (máximo 15 caracteres).");
        if (socios.buscarPorCorreoMovil(correo).filter(s -> !s.getId_socio().equals(socio.getId_socio())).isPresent())
            throw error(HttpStatus.CONFLICT, "Ese correo ya está registrado.");
        socio.setNom_socio(nombre); socio.setCorreo_socio(correo); socio.setTel_socio(tel);
        try { return perfilDe(socios.saveAndFlush(socio)); }
        catch (DataIntegrityViolationException e) { throw error(HttpStatus.CONFLICT, "No se pudieron guardar los datos."); }
    }

    @GetMapping("/membresia")
    public EstadoMembresia membresia(@RequestHeader(value="Authorization", required=false) String auth) {
        Socio socio = socioDe(auth);
        return calcularMembresia(membresias.findBySocioId(socio.getId_socio()), LocalDate.now(ZONA));
    }

    // Selecciona primero una vigente por fechas, incluso si hay otra compra futura.
    static EstadoMembresia calcularMembresia(List<Membresia> lista, LocalDate hoy) {
        Comparator<Membresia> orden = Comparator.comparing(Membresia::getFecha_f).thenComparing(Membresia::getId_mensualidad);
        Optional<Membresia> vigente = lista.stream().filter(m -> "VIGENTE".equals(m.getEstatus()) &&
                !hoy.isBefore(m.getFecha_i()) && !hoy.isAfter(m.getFecha_f())).max(orden);
        if (vigente.isPresent()) return estado(vigente.get(), "VIGENTE", hoy);
        Optional<Membresia> futura = lista.stream().filter(m -> "VIGENTE".equals(m.getEstatus()) && hoy.isBefore(m.getFecha_i()))
                .min(Comparator.comparing(Membresia::getFecha_i).thenComparing(Membresia::getId_mensualidad));
        if (futura.isPresent()) return estado(futura.get(), "PENDIENTE", hoy);
        return lista.stream().max(orden).map(m -> estado(m, "CANCELADA".equals(m.getEstatus()) ? "CANCELADA" : "VENCIDA", hoy))
                .orElse(new EstadoMembresia("SIN_MEMBRESIA", null, null, null, null, 0));
    }

    private static EstadoMembresia estado(Membresia m, String estado, LocalDate hoy) {
        return new EstadoMembresia(estado, m.getPlan().getNombre(), m.getFecha_i().toString(), m.getFecha_f().toString(),
                m.getPrecio_cobrado(), "VIGENTE".equals(estado) ? ChronoUnit.DAYS.between(hoy, m.getFecha_f()) : 0);
    }

    @GetMapping("/planes")
    public List<Plan> planes(@RequestHeader(value="Authorization", required=false) String auth) {
        socioDe(auth);
        return planes.findAll().stream().filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .sorted(Comparator.comparing(p -> p.getDuracion_dias()))
                .map(p -> new Plan(p.getId_plan(), p.getNombre(), p.getDuracion_dias(), p.getPrecio())).toList();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value="Authorization", required=false) String auth) {
        if (auth != null && auth.startsWith("Bearer ")) sesiones.remove(auth.substring(7));
        return ResponseEntity.noContent().build();
    }

    private Perfil perfilDe(Socio socio) {
        String primera = membresias.findBySocioId(socio.getId_socio()).stream().map(Membresia::getFecha_i)
                .min(LocalDate::compareTo).map(LocalDate::toString).orElse(null);
        return new Perfil(socio.getId_socio(), socio.getNom_socio(), socio.getCorreo_socio(), socio.getTel_socio(), socio.getEstatus(), primera);
    }
    private Socio socioDe(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) throw error(HttpStatus.UNAUTHORIZED, "Inicia sesión.");
        String token = auth.substring(7);
        Sesion sesion = sesiones.get(token);
        if (sesion == null || !sesion.expira().isAfter(Instant.now())) {
            sesiones.remove(token); throw error(HttpStatus.UNAUTHORIZED, "Tu sesión terminó. Inicia sesión otra vez.");
        }
        Socio socio = socios.findById(sesion.socioId()).orElse(null);
        if (!esSocio(socio)) { sesiones.remove(token); throw error(HttpStatus.UNAUTHORIZED, "La cuenta ya no está disponible."); }
        return socio;
    }
    // Un socio INACTIVO puede consultar su historial y precios; no implica autorización de entrada.
    private boolean esSocio(Socio s) { return s != null && s.getRol() != null && "SOCIO".equals(s.getRol().getNombre()); }
    private String limpio(String s) { return s == null ? "" : s.trim(); }
    private static ApiError error(HttpStatus status, String mensaje) { return new ApiError(status, mensaje); }
    private static class ApiError extends RuntimeException {
        final HttpStatus status;
        ApiError(HttpStatus status, String mensaje) { super(mensaje); this.status = status; }
    }
    @ExceptionHandler(ApiError.class)
    public ResponseEntity<Map<String,String>> responderError(ApiError e) {
        return ResponseEntity.status(e.status).body(Map.of("mensaje", e.getMessage()));
    }
}
