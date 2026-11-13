<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // 1. Lógica Java: Obtenemos los datos (simulando tu BD)
    int asisEne = 55;
    int asisFeb = 85;
    int asisMar = 85;
    int asisAbr = 115;
    int asisMay = 140;
    int asisJun = 105;

    // Convertimos a formato de arreglo para JS
    String datosParaJS = "[" + asisEne + ", " + asisFeb + ", " + asisMar + ", " + asisAbr + ", " + asisMay + ", " + asisJun + "]";
%>
<!doctype html>
<!-- Agregué el data-bs-theme="dark" para que mantenga el estilo oscuro de GymTrack -->
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Gymtrack</title>

    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="estilos.css" rel="stylesheet">

    <!-- 2. Chart.js CDN (Necesario para la gráfica) -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <!-- CONTENEDOR FLEX PRINCIPAL -->
    <div class="d-flex">

        <!-- Aquí se inyecta tu sidebar (280px de ancho fijos) -->
        <jsp:include page="navbar.jsp" />

        <!-- Contenido de la página (El w-100 hace que ocupe el resto de la pantalla) -->
        <div class="contenido w-100 p-4">

            <h2 class="mb-4">Panel de Control</h2>

            <!-- 3. Contenedor de la gráfica dentro del contenido principal -->
            <div class="card shadow border-0" style="max-width: 600px;">
                <div class="card-body">
                    <h5 class="card-title fw-bold mb-3">Resumen del Gimnasio</h5>
                    <!-- Lienzo donde se dibuja la gráfica -->
                    <canvas id="graficoGimnasio"></canvas>
                </div>
            </div>

        </div>

    </div>

    <!-- Bootstrap 5 JS Bundle CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- 4. Script de inicialización de la gráfica -->
    <script>
        // Inyectamos la variable desde Java
        const datosGimnasio = <%= datosParaJS %>;

        // Configuramos colores globales de Chart.js para que se vean bien en TEMA OSCURO
        Chart.defaults.color = '#adb5bd'; // Color gris claro para los textos de los meses y números
        Chart.defaults.borderColor = 'rgba(255, 255, 255, 0.1)'; // Color sutil para las líneas de fondo

        const ctx = document.getElementById('graficoGimnasio').getContext('2d');
        const chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
                datasets: [{
                    label: 'Asistencia',
                    data: datosGimnasio, // Aquí se usan los datos de Java
                    borderColor: '#3b82f6', // Color azul vibrante de la línea
                    backgroundColor: 'rgba(59, 130, 246, 0.2)', // Relleno azul con transparencia
                    borderWidth: 2,
                    pointBackgroundColor: '#3b82f6',
                    pointRadius: 4,
                    pointHoverRadius: 6,
                    fill: true,
                    tension: 0.4 // Curvatura
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 150,
                        ticks: { stepSize: 50 },
                        border: { display: false }
                    },
                    x: {
                        grid: { display: false }, // Ocultamos las líneas verticales
                        border: { display: false }
                    }
                }
            }
        });
    </script>
</body>
</html>