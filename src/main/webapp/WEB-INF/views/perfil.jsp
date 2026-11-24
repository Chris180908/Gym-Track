<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%!

    private String esc(String s) {
        if (s == null || s.trim().isEmpty()) return "—";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    GymTrack.example.demo.model.Recepcionista r =
            (GymTrack.example.demo.model.Recepcionista) request.getAttribute("recepcionista");
    String idRecepcion = String.valueOf(r.getId_recepcion());
    String nombre = r.getNom_recepcion();
    String numCalle = r.getNum_calle_r();
    String colonia = r.getCol_r();
    String estado = r.getEdo_r();
    String codPostal = r.getCod_post_r();
    String telefono = r.getTel_recepcion();
    String correo = r.getCorreo_recepcion();
    String foto = null;

    String iniciales = "";
    for (String p : nombre.trim().split("\\s+")) {
        if (!p.isEmpty() && iniciales.length() < 2) iniciales += Character.toUpperCase(p.charAt(0));
    }
%>
<!doctype html>
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil - Gymtrack</title>

    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/estilos.css" rel="stylesheet">


</head>
<body>
<div class="d-flex">

    <jsp:include page="navbar.jsp" />

    <div class="contenido w-100 p-4">
        <div class="container-fluid py-4">
            <h1 class="h2 fw-semibold mb-4">Perfil</h1>

            <div class="row g-4">

                <!-- Tarjeta de foto -->
                <div class="col-12 col-xl-4">
                    <div class="cuadro-panel h-100 text-center">
                        <div class="d-flex justify-content-center mb-3">
                            <% if (foto != null && !foto.isEmpty()) { %>
                                <img src="<%= esc(foto) %>" alt="Foto de perfil" class="avatar-perfil">
                            <% } else { %>
                                <div class="avatar-iniciales d-flex align-items-center justify-content-center">
                                    <%= esc(iniciales) %>
                                </div>
                            <% } %>
                        </div>
                        <h2 class="h5 mb-1"><%= esc(nombre) %></h2>
                        <span class="badge text-bg-primary mb-3">Recepcionista</span>
                    </div>
                </div>

                <!-- Tarjeta de datos -->
                <div class="col-12 col-xl-8">
                    <div class="cuadro-panel h-100">
                        <h2 class="h5 fw-normal mb-4">Datos del usuario</h2>

                        <div class="row">
                            <div class="col-12 col-md-6">
                                <div class="dato-etiqueta"><i class="bi bi-hash me-1"></i>ID</div>
                                <div class="dato-valor"><%= esc(idRecepcion) %></div>
                            </div>
                            <div class="col-12 col-md-6">
                                <div class="dato-etiqueta"><i class="bi bi-person me-1"></i>Nombre</div>
                                <div class="dato-valor"><%= esc(nombre) %></div>
                            </div>
                            <div class="col-12 col-md-6">
                                <div class="dato-etiqueta"><i class="bi bi-telephone me-1"></i>Teléfono</div>
                                <div class="dato-valor"><%= esc(telefono) %></div>
                            </div>
                            <div class="col-12 col-md-6">
                                <div class="dato-etiqueta"><i class="bi bi-envelope me-1"></i>Correo</div>
                                <div class="dato-valor"><%= esc(correo) %></div>
                            </div>
                            <div class="col-12">
                                <div class="dato-etiqueta"><i class="bi bi-geo-alt me-1"></i>Dirección</div>
                                <div class="dato-valor">
                                    <%= esc(numCalle) %>, <%= esc(colonia) %>, <%= esc(estado) %>, C.P. <%= esc(codPostal) %>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle CDN -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
