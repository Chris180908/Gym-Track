<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="Panel de Control de Recepción - GymTrack"/>
    <jsp:param name="activo" value="inicio"/>
    <jsp:param name="subtitulo" value="Hola, ${sessionScope.nombre}. Hoy es ${hoyTexto}."/>
</jsp:include>

<%-- Tarjetas de resumen --%>
<div class="row g-3 mb-4">
    <div class="col-12 col-md-6 col-xl-3">
        <div class="cuadro-panel h-100 d-flex align-items-center">
            <div class="bg-primary bg-opacity-10 text-primary rounded-3 p-3 me-3"><i class="bi bi-people-fill fs-3"></i></div>
            <div>
                <div class="text-secondary small">Socios activos</div>
                <div class="fs-4 fw-bold lh-1"><c:out value="${activos}"/></div>
                <div class="text-secondary small">de <c:out value="${totalSocios}"/> registrados</div>
            </div>
        </div>
    </div>
    <div class="col-12 col-md-6 col-xl-3">
        <div class="cuadro-panel h-100 d-flex align-items-center">
            <div class="bg-success bg-opacity-10 text-success rounded-3 p-3 me-3"><i class="bi bi-card-checklist fs-3"></i></div>
            <div>
                <div class="text-secondary small">Membresías vigentes</div>
                <div class="fs-4 fw-bold"><c:out value="${membresiasVigentes}"/></div>
            </div>
        </div>
    </div>
    <div class="col-12 col-md-6 col-xl-3">
        <div class="cuadro-panel h-100 d-flex align-items-center">
            <div class="bg-danger bg-opacity-10 text-danger rounded-3 p-3 me-3"><i class="bi bi-hourglass-split fs-3"></i></div>
            <div>
                <div class="text-secondary small">Vencen en 7 días</div>
                <div class="fs-4 fw-bold"><c:out value="${vencen7}"/></div>
            </div>
        </div>
    </div>
    <div class="col-12 col-md-6 col-xl-3">
        <div class="cuadro-panel h-100 d-flex align-items-center">
            <div class="bg-warning bg-opacity-10 text-warning rounded-3 p-3 me-3"><i class="bi bi-cash-coin fs-3"></i></div>
            <div>
                <div class="text-secondary small">Ingresos del mes</div>
                <div class="fs-4 fw-bold">$<fmt:formatNumber value="${ingresosMes}" pattern="#,##0.00"/></div>
            </div>
        </div>
    </div>
</div>

<%-- Accesos rápidos --%>
<h2 class="h5 mb-3">Accesos rápidos</h2>
<div class="row g-3 mb-4">
    <div class="col-12 col-md-6 col-xl-3">
        <a href="${ctx}/socios/nuevo" class="cuadro-panel h-100 d-block text-decoration-none text-reset">
            <i class="bi bi-person-plus fs-3 text-primary"></i>
            <div class="fw-semibold mt-2">Registrar nuevo socio</div>
            <div class="small text-secondary">Da de alta a un socio con sus datos de contacto</div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-xl-3">
        <a href="${ctx}/membresias/nuevo" class="cuadro-panel h-100 d-block text-decoration-none text-reset">
            <i class="bi bi-person-vcard fs-3 text-success"></i>
            <div class="fw-semibold mt-2">Asignar o renovar membresía</div>
            <div class="small text-secondary">Membresías de los socios y planes disponibles</div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-xl-3">
        <a href="${ctx}/pagos/nuevo" class="cuadro-panel h-100 d-block text-decoration-none text-reset">
            <i class="bi bi-cash-stack fs-3 text-warning"></i>
            <div class="fw-semibold mt-2">Registrar pago</div>
            <div class="small text-secondary">Cobra una membresía en efectivo, tarjeta o transferencia</div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-xl-3">
        <a href="${ctx}/reportes" class="cuadro-panel h-100 d-block text-decoration-none text-reset">
            <i class="bi bi-bar-chart fs-3 text-info"></i>
            <div class="fw-semibold mt-2">Generar reporte</div>
            <div class="small text-secondary">Consulta pagos, membresías y socios por fechas</div>
        </a>
    </div>
</div>

<div class="row g-4">
    <%-- Membresías por vencer --%>
    <div class="col-12 col-xl-8">
        <div class="cuadro-panel h-100">
            <h2 class="h5 mb-3">Membresías por vencer (próximos 7 días)</h2>
            <div class="table-responsive">
                <table class="table align-middle mb-0">
                    <thead>
                        <tr><th>Socio</th><th>Plan</th><th>Vence</th><th class="text-end">Acción</th></tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${porVencer}" var="m">
                            <tr>
                                <td><c:out value="${m.socio.nom_socio}"/></td>
                                <td><c:out value="${m.plan.nombre}"/></td>
                                <td><c:out value="${m.fecha_f}"/></td>
                                <td class="text-end">
                                    <a href="${ctx}/membresias/editar/${m.id_mensualidad}" class="btn btn-sm btn-outline-success">
                                        <i class="bi bi-arrow-repeat"></i> Renovar
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty porVencer}">
                            <tr><td colspan="4" class="text-center text-secondary py-4">No hay membresías por vencer esta semana</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="col-12 col-xl-4 d-flex flex-column gap-4">
        <%-- Búsqueda rápida --%>
        <div class="cuadro-panel">
            <h2 class="h6 fw-semibold mb-3">Búsqueda de socio rápida</h2>
            <form action="${ctx}/inicio" method="get">
                <input type="text" name="q" value="<c:out value='${q}'/>" class="form-control" placeholder="ID, nombre, correo o teléfono">
                <button type="submit" class="btn btn-primary w-100 mt-3">Buscar</button>
            </form>

            <c:if test="${not empty q}">
                <hr>
                <c:forEach items="${resultados}" var="s">
                    <a href="${ctx}/socios/editar/${s.id_socio}" class="d-block text-decoration-none text-reset mb-2">
                        <div class="fw-semibold">#<c:out value="${s.id_socio}"/> · <c:out value="${s.nom_socio}"/></div>
                        <div class="small text-secondary"><c:out value="${s.correo_socio}"/> · <c:out value="${s.tel_socio}"/></div>
                    </a>
                </c:forEach>
                <c:if test="${empty resultados}">
                    <div class="text-secondary small">No se encontró ningún socio.</div>
                </c:if>
            </c:if>
        </div>

        <%-- Últimos pagos --%>
        <div class="cuadro-panel">
            <h2 class="h6 fw-semibold mb-3">Últimos pagos</h2>
            <c:forEach items="${ultimosPagos}" var="p">
                <div class="d-flex justify-content-between align-items-center border-bottom py-2">
                    <div>
                        <div class="fw-semibold"><c:out value="${p.membresia.socio.nom_socio}"/></div>
                        <div class="small text-secondary">
                            <c:out value="${fn:replace(fn:substring(p.fecha_pago, 0, 16), 'T', ' ')}"/>
                            <span class="badge ${p.metodo_pago == 'EFECTIVO' ? 'text-bg-success' : (p.metodo_pago == 'TARJETA' ? 'text-bg-info' : 'text-bg-secondary')}"><c:out value="${p.metodo_pago}"/></span>
                        </div>
                    </div>
                    <div class="text-success fw-bold">$<fmt:formatNumber value="${p.monto}" pattern="#,##0.00"/></div>
                </div>
            </c:forEach>
            <c:if test="${empty ultimosPagos}"><div class="text-secondary small">Aún no hay pagos registrados.</div></c:if>
        </div>
    </div>
</div>

<jsp:include page="pie.jsp"/>
</body>
</html>
