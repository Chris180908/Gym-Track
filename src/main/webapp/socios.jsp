<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<!-- Agregué el data-bs-theme="dark" para que mantenga el estilo oscuro de GymTrack -->
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
    <link href="estilos.css" rel="stylesheet">
</head>
<body>
    <!-- CONTENEDOR FLEX PRINCIPAL -->
    <div class="d-flex">

        <!-- Aquí se inyecta tu sidebar (280px de ancho fijos) -->
        <jsp:include page="navbar.jsp" />

        <!-- Contenido de la página (El w-100 hace que ocupe el resto de la pantalla) -->
        <div class="contenido w-100 p-4">
            <h1>Bienvenido a Gymtrack</h1>
            <p>Hola, este es el contenido de tu página.</p>
            <a href="modulitoR.jsp" class="btn btn-primary">Hola</a>
        </div>

    </div>

    <!-- Bootstrap 5 JS Bundle CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>