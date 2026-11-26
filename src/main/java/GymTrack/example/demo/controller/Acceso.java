package GymTrack.example.demo.controller;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import GymTrack.example.demo.Service.RecepcionistaService;
import GymTrack.example.demo.model.Recepcionista;

public class Acceso {
    public static boolean sesion(HttpSession session) {
        return session.getAttribute("idRecepcion") != null &&
            ("ADMIN".equals(session.getAttribute("rol")) || "RECEPCIONISTA".equals(session.getAttribute("rol")));
    }
    public static boolean sesion(HttpSession session, RecepcionistaService servicio) {
        if (!sesion(session)) return false;
        Recepcionista r = servicio.findById(id(session)).orElse(null);
        if (r == null || r.getRol() == null || !r.getRol().getNombre().equals(session.getAttribute("rol"))) {
            session.invalidate();
            return false;
        }
        return true;
    }
    public static boolean admin(HttpSession session) { return sesion(session) && "ADMIN".equals(session.getAttribute("rol")); }
    public static Integer id(HttpSession session) { return (Integer) session.getAttribute("idRecepcion"); }
    public static void token(HttpSession session) {
        if (session.getAttribute("token") == null) session.setAttribute("token", UUID.randomUUID().toString());
    }
    public static boolean post(HttpSession session, String token) {
        return token != null && token.equals(session.getAttribute("token"));
    }
}
