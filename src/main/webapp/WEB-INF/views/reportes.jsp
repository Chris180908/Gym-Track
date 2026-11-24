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
            <h1 class="h2 mb-4">Reportes y pagos</h1>
            <div class="cuadro-panel mb-4">
                <h2 class="h5 mb-3">Pagos</h2>
                <div class="table-responsive">
                    <table class="table table-dark table-striped align-middle">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Socio</th>
                                <th>Monto</th>
                                <th>Fecha</th>
                                <th>Método</th>
                                <th>Origen</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${pagos}" var="fila">
                                <tr>
                                    <td><c:out value="${fila.id_pago}"/></td>
                                    <td><c:out value="${fila.membresia.socio.nom_socio}"/></td>
                                    <td><c:out value="${fila.monto}"/></td>
                                    <td><c:out value="${fila.fecha_pago}"/></td>
                                    <td><c:out value="${fila.metodo_pago}"/></td>
                                    <td><c:out value="${fila.origen}"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty pagos}">
                                <tr><td colspan="6" class="text-secondary">No hay registros.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="cuadro-panel mb-4">
                <h2 class="h5 mb-3">Membresías</h2>
                <div class="table-responsive">
                    <table class="table table-dark table-striped align-middle">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Socio</th>
                                <th>Plan</th>
                                <th>Inicio</th>
                                <th>Fin</th>
                                <th>Precio</th>
                                <th>Estado actual</th>
                                <th>Modalidad</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${membresias}" var="fila">
                                <tr>
                                    <td><c:out value="${fila.id_mensualidad}"/></td>
                                    <td><c:out value="${fila.socio.nom_socio}"/></td>
                                    <td><c:out value="${fila.tipo}"/></td>
                                    <td><c:out value="${fila.fecha_i}"/></td>
                                    <td><c:out value="${fila.fecha_f}"/></td>
                                    <td><c:out value="${fila.precio}"/></td>
                                    <td><c:out value="${fila.estatusActual}"/></td>
                                    <td><c:out value="${fila.modalidad}"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty membresias}">
                                <tr><td colspan="8" class="text-secondary">No hay registros.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="cuadro-panel mb-4">
                <h2 class="h5 mb-3">Socios</h2>
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
            <div class="cuadro-panel mb-4">
                <h2 class="h5 mb-3">Reportes guardados</h2>
                <div class="table-responsive">
                    <table class="table table-dark table-striped align-middle">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tipo</th>
                                <th>Desde</th>
                                <th>Hasta</th>
                                <th>Fecha</th>
                                <th>Recepcionista</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${reportes}" var="fila">
                                <tr>
                                    <td><c:out value="${fila.id_reporte}"/></td>
                                    <td><c:out value="${fila.tipo_reporte}"/></td>
                                    <td><c:out value="${fila.fecha_inicio_filtro}"/></td>
                                    <td><c:out value="${fila.fecha_fin_filtro}"/></td>
                                    <td><c:out value="${fila.fecha_g}"/></td>
                                    <td><c:out value="${fila.recepcionista.nom_recepcion}"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty reportes}">
                                <tr><td colspan="6" class="text-secondary">No hay registros.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            <p class="text-secondary">Registro de reportes disponible en la API REST. Los filtros de pantalla están pendientes.</p>
        </main>
    </div>
</body>
</html>
