<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!doctype html>
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GymTrack</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/estilos.css" rel="stylesheet">
</head>
<body>
    <div class="d-flex">
        <jsp:include page="navbar.jsp" />
        <main class="contenido w-100 p-4">
            <h1 class="h2 mb-4">Panel de socios</h1>
            <div class="cuadro-panel mb-4">
                <h2 class="h5 mb-3">Usuarios</h2>
                <div class="table-responsive">
                    <table class="table table-dark table-striped align-middle">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Teléfono</th>
                                <th>Correo</th>
                                <th>Calle</th>
                                <th>Colonia</th>
                                <th>Estado</th>
                                <th>CP</th>
                                <th>Estatus</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${socios}" var="fila">
                                <tr>
                                    <td><c:out value="${fila.id_socio}"/></td>
                                    <td><c:out value="${fila.nom_socio}"/></td>
                                    <td><c:out value="${fila.tel_socio}"/></td>
                                    <td><c:out value="${fila.correo_socio}"/></td>
                                    <td><c:out value="${fila.num_calle_s}"/></td>
                                    <td><c:out value="${fila.col_s}"/></td>
                                    <td><c:out value="${fila.edo_s}"/></td>
                                    <td><c:out value="${fila.cod_post_s}"/></td>
                                    <td><c:out value="${fila.estatus}"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty socios}">
                                <tr><td colspan="9" class="text-secondary">No hay registros.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            <p class="text-secondary">Las altas, cambios y bajas se pueden probar desde la API REST.</p>
        </main>
    </div>
</body>
</html>
