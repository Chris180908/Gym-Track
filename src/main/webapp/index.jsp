<!DOCTYPE html>
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Please sign in</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons CDN (para el logo B) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="estilos.css" rel="stylesheet">
</head>
<body class="d-flex align-items-center justify-content-center min-vh-100 m-0">

    <main class="form-signin w-100 m-auto text-center">
        <form action = "modulito.jsp">

            <h1 class="h3 mb-3 fw-normal">Bienvenido a Gymtrack</h1>

            <!-- Floating Labels agrupados -->
            <div class="form-signin-inputs">
                <div class="form-floating">
                    <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com">
                    <label for="floatingInput">Usuario</label>
                </div>
                <div class="form-floating">
                    <input type="password" class="form-control" id="floatingPassword" placeholder="Password">
                    <label for="floatingPassword">Contrasena</label>
                </div>
            </div>

            <div class="form-check text-start my-3 ms-1">
                <input class="form-check-input" type="checkbox" value="remember-me" id="flexCheckDefault">
                <label class="form-check-label text-body-secondary fs-6" for="flexCheckDefault">
                    Mantener iniciada la cuenta
                </label>
            </div>

            <button class="btn btn-primary w-100 py-2 btn-login fw-semibold" type="submit"> Ingresar </button>

        </form>
    </main>

    <!-- Bootstrap 5 JS Bundle CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>