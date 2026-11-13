<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%

    String paginaActual = request.getServletPath();
    if (paginaActual == null) paginaActual = "";
%>

<div class="d-flex flex-column flex-shrink-0 p-3 bg-body-tertiary" style="width: 280px; height: 100vh;">
    <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis text-decoration-none">
        <span class="fs-4">GYMTRACK</span>
    </a>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item mb-1">
            <a href="inicio.jsp" class="nav-link <%= paginaActual.endsWith("inicio.jsp") ? "active" : "link-body-emphasis" %>">
                <i class="bi bi-house-door me-2"></i> Inicio
            </a>
        </li>
        <li class="nav-item mb-1">
            <a href="socios.jsp" class="nav-link <%= paginaActual.endsWith("socios.jsp") ? "active" : "link-body-emphasis" %>">
                <i class="bi bi-people me-2"></i> Socios
            </a>
        </li>
        <li class="nav-item mb-1">
            <a href="membresias.jsp" class="nav-link <%= paginaActual.endsWith("membresias.jsp") ? "active" : "link-body-emphasis" %>">
                <i class="bi bi-person-vcard me-2"></i> Membresias
            </a>
        </li>
        <li class="nav-item mb-1">
            <a href="reportes.jsp" class="nav-link <%= paginaActual.endsWith("reportes.jsp") ? "active" : "link-body-emphasis" %>">
                <i class="bi bi-grid me-2"></i> Reportes
            </a>
        </li>
        <li class="nav-item">
            <a href="perfil.jsp" class="nav-link <%= paginaActual.endsWith("perfil.jsp") ? "active" : "link-body-emphasis" %>">
                <i class="bi bi-person me-2"></i> Perfil
            </a>
        </li>
    </ul>
    <hr>
</div>