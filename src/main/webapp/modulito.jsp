<!DOCTYPE html>
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

    <!-- Sidebar -->
    <div class="d-flex flex-column flex-shrink-0 p-3 bg-body-tertiary" style="width: 280px; height: 100vh;">
        <!-- Cabecera del Sidebar con el logo B y el título -->
        <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis text-decoration-none">
            <span class="fs-4">Menu</span>
        </a>
        <hr>

        <!-- Lista de Navegación -->
        <ul class="nav nav-pills flex-column mb-auto">
            <li class="nav-item mb-1">
                <a href="#" class="nav-link active" aria-current="page">
                    <i class="bi bi-house-door me-2"></i> Home
                </a>
            </li>
            <li class="nav-item mb-1">
                <a href="#" class="nav-link link-body-emphasis">
                    <i class="bi bi-speedometer2 me-2"></i> Dashboard
                </a>
            </li>
            <li class="nav-item mb-1">
                <a href="#" class="nav-link link-body-emphasis">
                    <i class="bi bi-table me-2"></i> Orders
                </a>
            </li>
            <li class="nav-item mb-1">
                <a href="#" class="nav-link link-body-emphasis">
                    <i class="bi bi-grid me-2"></i> Products
                </a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link link-body-emphasis">
                    <i class="bi bi-person-circle me-2"></i> Customers
                </a>
            </li>
        </ul>

        <hr>

        <!-- Perfil de usuario en la parte inferior -->
        <div class="dropdown">
            <a href="#" class="d-flex align-items-center link-body-emphasis text-decoration-none" data-bs-toggle="dropdown" aria-expanded="false">
                <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32" class="rounded-circle me-2">
                <strong>mdo</strong>
            </a>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>