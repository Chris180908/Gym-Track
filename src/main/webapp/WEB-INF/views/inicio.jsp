<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!doctype html>
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gymtrack</title>

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
                <h1 class="h2 fw-semibold mb-4">Panel de Control de Recepción - GymTrack</h1>

                <div class="row g-4">


                    <div class="col-12 col-xl-8">
                        <div class="cuadro-panel h-100">
                            <h2 class="h5 fw-normal mb-3">Socios registrados</h2>
                            <p class="text-secondary">${socios.size()} socios registrados.</p>
                            <a href="${pageContext.request.contextPath}/socios" class="btn btn-primary">Consultar socios</a>
                        </div>
                    </div>

                    <div class="col-12 col-xl-4 d-flex flex-column gap-4">

                           <div class="cuadro-panel">
                             <h2 class="h6 fw-semibold mb-3">Acceso rápido a membresías</h2>
                             <a href="${pageContext.request.contextPath}/membresias" class="btn btn-outline-primary">Consultar membresías</a>
                           </div>

                           <div class="cuadro-panel">
                           <h2 class="h6 fw-semibold mb-3">Reportes y pagos</h2>
                           <a href="${pageContext.request.contextPath}/reportes" class="btn btn-primary">Consultar reportes</a>
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