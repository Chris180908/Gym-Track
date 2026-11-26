<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión - GymTrack</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="${ctx}/estilos.css" rel="stylesheet">
</head>
<body class="d-flex align-items-center justify-content-center min-vh-100 m-0">

    <main class="form-signin w-100 m-auto text-center">
        <form action="${ctx}/login" method="post">
            <input type="hidden" name="token" value="${sessionScope.token}">

            <i class="bi bi-lightning-charge-fill text-primary display-4"></i>
            <h1 class="h3 mb-1 fw-normal">Bienvenido a GymTrack</h1>
            <p class="text-secondary mb-4">Acceso para personal del gimnasio</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger py-2" role="alert"><c:out value="${error}"/></div>
            </c:if>

            <div class="form-signin-inputs">
                <div class="form-floating">
                    <input type="email" class="form-control" id="floatingInput" name="username" placeholder="name@example.com" required autofocus>
                    <label for="floatingInput">Correo electrónico</label>
                </div>
                <div class="form-floating">
                    <input type="password" class="form-control" id="floatingPassword" name="password" placeholder="Contraseña" required>
                    <label for="floatingPassword">Contraseña</label>
                </div>
            </div>

            <button class="btn btn-primary w-100 py-2 btn-login fw-semibold mt-3" type="submit">Ingresar</button>
        </form>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
