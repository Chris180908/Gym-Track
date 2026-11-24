<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gymtrack</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons CDN (para el logo B) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/estilos.css" rel="stylesheet">
</head>
<body class="d-flex align-items-center justify-content-center min-vh-100 m-0">

    <main class="form-signin w-100 m-auto text-center">
        <form action="${pageContext.request.contextPath}/login" method="post">

            <h1 class="h3 mb-3 fw-normal">Bienvenido a Gymtrack</h1>

            <c:if test="${not empty error}">
                <div class="alert alert-danger py-2" role="alert">
                    ${error}
                </div>
            </c:if>

            <!-- Floating Labels agrupados -->
            <div class="form-signin-inputs">
                <div class="form-floating">
                    <input type="email" class="form-control" id="floatingInput" name="username" placeholder="name@example.com" required>
                    <label for="floatingInput">Correo electrónico</label>
                </div>
                <div class="form-floating">
                    <input type="password" class="form-control" id="floatingPassword" name="password" placeholder="Password" required>
                    <label for="floatingPassword">Contraseña</label>
                </div>
            </div>

            <button class="btn btn-primary w-100 py-2 btn-login fw-semibold mt-3" type="submit"> Ingresar </button>

        </form>
    </main>

    <!-- Bootstrap 5 JS Bundle CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>