<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="Consultas y Reportes"/>
    <jsp:param name="activo" value="reportes"/>
    <jsp:param name="subtitulo" value="Actividad del gimnasio: socios, membresías e ingresos"/>
</jsp:include>

<%-- Tarjetas de resumen --%>
<div class="row g-3 mb-4">
    <div class="col-12 col-md-4">
        <div class="cuadro-panel h-100 d-flex align-items-center">
            <div class="bg-primary bg-opacity-10 text-primary rounded-3 p-3 me-3"><i class="bi bi-people-fill fs-3"></i></div>
            <div><span class="text-secondary small d-block">Socios activos</span><h4 class="fw-bold mb-0"><c:out value="${totalSocios}"/></h4></div>
        </div>
    </div>
    <div class="col-12 col-md-4">
        <div class="cuadro-panel h-100 d-flex align-items-center">
            <div class="bg-success bg-opacity-10 text-success rounded-3 p-3 me-3"><i class="bi bi-card-checklist fs-3"></i></div>
            <div><span class="text-secondary small d-block">Membresías vigentes</span><h4 class="fw-bold mb-0"><c:out value="${membresiasVigentes}"/></h4></div>
        </div>
    </div>
    <div class="col-12 col-md-4">
        <div class="cuadro-panel h-100 d-flex align-items-center">
            <div class="bg-warning bg-opacity-10 text-warning rounded-3 p-3 me-3"><i class="bi bi-cash-coin fs-3"></i></div>
            <div><span class="text-secondary small d-block">Ingresos del periodo</span><h4 class="fw-bold mb-0">$<fmt:formatNumber value="${ingresosTotales}" pattern="#,##0.00"/></h4></div>
        </div>
    </div>
</div>

<%-- Filtros: consultar (GET) o, si se está editando un reporte del historial, actualizarlo (POST) --%>
<div class="cuadro-panel mb-4 no-print">
    <h2 class="h6 fw-semibold mb-3"><i class="bi bi-funnel me-1"></i>
        <c:choose><c:when test="${not empty idEditar}">Editando el reporte #<c:out value="${idEditar}"/></c:when><c:otherwise>Filtros de consulta</c:otherwise></c:choose>
    </h2>

    <c:choose>
        <c:when test="${not empty idEditar}"><c:set var="accionForm" value="${ctx}/reportes/guardar"/><c:set var="metodoForm" value="post"/></c:when>
        <c:otherwise><c:set var="accionForm" value="${ctx}/reportes"/><c:set var="metodoForm" value="get"/></c:otherwise>
    </c:choose>

    <form action="${accionForm}" method="${metodoForm}" class="row g-3 align-items-end">
        <c:if test="${not empty idEditar}">
            <input type="hidden" name="token" value="${sessionScope.token}">
            <input type="hidden" name="id" value="${idEditar}">
        </c:if>
        <div class="col-md-3">
            <label for="tipoReporte" class="form-label small text-secondary">Tipo de reporte</label>
            <select class="form-select" id="tipoReporte" name="tipoReporte">
                <option value="GENERAL" ${tipoReporte == 'GENERAL' ? 'selected' : ''}>Actividad general</option>
                <option value="SOCIOS" ${tipoReporte == 'SOCIOS' ? 'selected' : ''}>Socios</option>
                <option value="MEMBRESIAS" ${tipoReporte == 'MEMBRESIAS' ? 'selected' : ''}>Membresías</option>
                <option value="PAGOS" ${tipoReporte == 'PAGOS' ? 'selected' : ''}>Pagos e ingresos</option>
            </select>
        </div>
        <div class="col-md-3">
            <label for="fechaInicio" class="form-label small text-secondary">Fecha inicio</label>
            <input type="date" class="form-control" id="fechaInicio" name="fechaInicio" value="${fechaInicio}">
        </div>
        <div class="col-md-3">
            <label for="fechaFin" class="form-label small text-secondary">Fecha fin</label>
            <input type="date" class="form-control" id="fechaFin" name="fechaFin" value="${fechaFin}">
        </div>
        <div class="col-md-3 d-flex gap-2">
            <c:choose>
                <c:when test="${not empty idEditar}">
                    <button type="submit" class="btn btn-success w-100"><i class="bi bi-check-lg me-1"></i> Actualizar</button>
                    <a href="${ctx}/reportes" class="btn btn-secondary">Cancelar</a>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-primary w-100"><i class="bi bi-search me-1"></i> Consultar</button>
                    <button type="button" class="btn btn-outline-light" onclick="window.print()" title="Imprimir / PDF"><i class="bi bi-printer"></i></button>
                </c:otherwise>
            </c:choose>
        </div>
    </form>

    <c:if test="${empty idEditar}">
        <%-- Guarda en el historial lo que se está viendo --%>
        <form action="${ctx}/reportes/guardar" method="post" class="mt-3 pt-3 border-top d-flex justify-content-between align-items-center">
            <input type="hidden" name="token" value="${sessionScope.token}">
            <input type="hidden" name="tipoReporte" value="${tipoReporte}">
            <input type="hidden" name="fechaInicio" value="${fechaInicio}">
            <input type="hidden" name="fechaFin" value="${fechaFin}">
            <span class="small text-secondary">¿Quieres conservar esta consulta (tipo y fechas) en el historial?</span>
            <button type="submit" class="btn btn-sm btn-success"><i class="bi bi-file-earmark-plus me-1"></i> Guardar en historial</button>
        </form>
    </c:if>
</div>

<%-- Resultados --%>
<div class="cuadro-panel">
    <ul class="nav nav-tabs mb-3 no-print" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link ${tipoReporte == 'MEMBRESIAS' or tipoReporte == 'SOCIOS' ? '' : 'active'}" data-bs-toggle="tab" data-bs-target="#pagos-pane" type="button" role="tab"><i class="bi bi-credit-card me-1"></i> Pagos</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link ${tipoReporte == 'MEMBRESIAS' ? 'active' : ''}" data-bs-toggle="tab" data-bs-target="#membresias-pane" type="button" role="tab"><i class="bi bi-person-badge me-1"></i> Membresías</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link ${tipoReporte == 'SOCIOS' ? 'active' : ''}" data-bs-toggle="tab" data-bs-target="#socios-pane" type="button" role="tab"><i class="bi bi-people me-1"></i> Socios</button>
        </li>
        <li class="nav-item ms-auto" role="presentation">
            <button class="nav-link text-warning" data-bs-toggle="tab" data-bs-target="#historial-pane" type="button" role="tab"><i class="bi bi-archive me-1"></i> Historial de reportes</button>
        </li>
    </ul>

    <div class="row mb-3 no-print">
        <div class="col-md-4 ms-auto">
            <div class="input-group input-group-sm">
                <span class="input-group-text"><i class="bi bi-search"></i></span>
                <input type="text" id="buscadorTabla" class="form-control" placeholder="Filtrar resultados en pantalla...">
            </div>
        </div>
    </div>

    <div class="tab-content">

        <%-- PAGOS --%>
        <div class="tab-pane fade ${tipoReporte == 'MEMBRESIAS' or tipoReporte == 'SOCIOS' ? '' : 'show active'}" id="pagos-pane" role="tabpanel">
            <h5 class="mb-3">Detalle de pagos</h5>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead><tr><th>ID</th><th>Socio</th><th>Concepto</th><th>Método</th><th>Fecha</th><th>Monto</th><th>Recepcionista</th></tr></thead>
                    <tbody>
                        <c:forEach items="${pagos}" var="p">
                            <tr>
                                <td>#<c:out value="${p.id_pago}"/></td>
                                <td><c:out value="${p.membresia.socio.nom_socio}"/></td>
                                <td><c:out value="${p.concepto}"/></td>
                                <td><span class="badge ${p.metodo_pago == 'EFECTIVO' ? 'text-bg-success' : (p.metodo_pago == 'TARJETA' ? 'text-bg-info' : 'text-bg-secondary')}"><c:out value="${p.metodo_pago}"/></span></td>
                                <td><c:out value="${fn:replace(fn:substring(p.fecha_pago, 0, 16), 'T', ' ')}"/></td>
                                <td class="text-success fw-bold">$<fmt:formatNumber value="${p.monto}" pattern="#,##0.00"/></td>
                                <td><c:out value="${p.recepcionista.nom_recepcion}"/></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty pagos}"><tr><td colspan="7" class="text-center text-secondary py-4">Sin pagos en el periodo</td></tr></c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- MEMBRESIAS --%>
        <div class="tab-pane fade ${tipoReporte == 'MEMBRESIAS' ? 'show active' : ''}" id="membresias-pane" role="tabpanel">
            <h5 class="mb-3">Estado de membresías</h5>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead><tr><th>ID</th><th>Socio</th><th>Plan</th><th>Inicio</th><th>Vencimiento</th><th>Precio</th><th>Estatus</th></tr></thead>
                    <tbody>
                        <c:forEach items="${membresias}" var="m">
                            <tr>
                                <td>#<c:out value="${m.id_mensualidad}"/></td>
                                <td><c:out value="${m.socio.nom_socio}"/></td>
                                <td><c:out value="${m.plan.nombre}"/></td>
                                <td><c:out value="${m.fecha_i}"/></td>
                                <td><c:out value="${m.fecha_f}"/></td>
                                <td>$<fmt:formatNumber value="${m.precio_cobrado}" pattern="#,##0.00"/></td>
                                <td><span class="badge ${m.estatus == 'VIGENTE' ? 'text-bg-success' : (m.estatus == 'VENCIDA' ? 'text-bg-danger' : 'text-bg-secondary')}"><c:out value="${m.estatus}"/></span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty membresias}"><tr><td colspan="7" class="text-center text-secondary py-4">Sin membresías en el periodo</td></tr></c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- SOCIOS --%>
        <div class="tab-pane fade ${tipoReporte == 'SOCIOS' ? 'show active' : ''}" id="socios-pane" role="tabpanel">
            <h5 class="mb-3">Directorio de socios</h5>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead><tr><th>ID</th><th>Nombre</th><th>Teléfono</th><th>Correo</th><th>Dirección</th><th>Estatus</th></tr></thead>
                    <tbody>
                        <c:forEach items="${socios}" var="s">
                            <tr>
                                <td><c:out value="${s.id_socio}"/></td>
                                <td><c:out value="${s.nom_socio}"/></td>
                                <td><c:out value="${s.tel_socio}"/></td>
                                <td><c:out value="${s.correo_socio}"/></td>
                                <td><c:out value="${s.num_calle_s} ${s.col_s} ${s.edo_s} ${s.cod_post_s}"/></td>
                                <td><span class="badge ${s.estatus == 'ACTIVO' ? 'text-bg-success' : 'text-bg-secondary'}"><c:out value="${s.estatus}"/></span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty socios}"><tr><td colspan="6" class="text-center text-secondary py-4">Sin socios registrados</td></tr></c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- HISTORIAL --%>
        <div class="tab-pane fade" id="historial-pane" role="tabpanel">
            <h5 class="mb-3">Bitácora de reportes generados</h5>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead><tr><th>ID</th><th>Tipo</th><th>Periodo consultado</th><th>Fecha de generación</th><th>Generado por</th><th class="text-end no-print">Acciones</th></tr></thead>
                    <tbody>
                        <c:forEach items="${reportes}" var="r">
                            <tr>
                                <td>#<c:out value="${r.id_reporte}"/></td>
                                <td><c:out value="${r.tipo_reporte}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty r.fecha_inicio_filtro and empty r.fecha_fin_filtro}">Sin filtro de fechas</c:when>
                                        <c:otherwise>
                                            <c:out value="${empty r.fecha_inicio_filtro ? 'Inicio' : r.fecha_inicio_filtro}"/> al <c:out value="${empty r.fecha_fin_filtro ? 'hoy' : r.fecha_fin_filtro}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td><c:out value="${fn:replace(fn:substring(r.fecha_g, 0, 16), 'T', ' ')}"/></td>
                                <td><c:out value="${r.recepcionista.nom_recepcion}"/></td>
                                <td class="text-end text-nowrap no-print">
                                    <a href="${ctx}/reportes/ver/${r.id_reporte}" class="btn btn-sm btn-outline-info" title="Ver"><i class="bi bi-eye"></i></a>
                                    <a href="${ctx}/reportes/editar/${r.id_reporte}" class="btn btn-sm btn-outline-primary" title="Editar"><i class="bi bi-pencil"></i></a>
                                    <button type="button" class="btn btn-sm btn-outline-danger" title="Eliminar"
                                            data-bs-toggle="modal" data-bs-target="#modalEliminar"
                                            data-url="${ctx}/reportes/eliminar/${r.id_reporte}"
                                            data-nombre="el reporte #${r.id_reporte}">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty reportes}"><tr><td colspan="6" class="text-center text-secondary py-4">Aún no hay reportes guardados</td></tr></c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="pie.jsp"/>
<jsp:include page="modal-eliminar.jsp"/>

<script>
    // Filtro rápido en pantalla sobre la pestaña activa
    document.getElementById('buscadorTabla').addEventListener('keyup', function () {
        const texto = this.value.toLowerCase();
        document.querySelectorAll('.tab-pane.active tbody tr').forEach(function (fila) {
            fila.style.display = fila.textContent.toLowerCase().includes(texto) ? '' : 'none';
        });
    });
</script>
</body>
</html>
