<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="Membresías"/>
    <jsp:param name="activo" value="membresias"/>
    <jsp:param name="subtitulo" value="Asigna y renueva membresías; revisa los ingresos por mes"/>
</jsp:include>

<div class="row g-4 mb-4">
    <%-- Gráfica de ingresos por mes (Chart.js) --%>
    <div class="col-12 col-xl-8">
        <div class="cuadro-panel h-100">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2 class="h5 fw-semibold mb-0">Ingresos por mes</h2>
                <form action="${ctx}/membresias" method="get">
                    <select name="anio" class="form-select form-select-sm" onchange="this.form.submit()" aria-label="Año">
                        <c:forEach items="${anios}" var="a">
                            <option value="${a}" ${a == anio ? 'selected' : ''}>${a}</option>
                        </c:forEach>
                    </select>
                </form>
            </div>
            <canvas id="graficoIngresos" height="110"></canvas>
        </div>
    </div>

    <div class="col-12 col-xl-4 d-flex flex-column gap-4">
        <div class="cuadro-panel">
            <div class="text-secondary small">Ingresos del año</div>
            <div class="fs-3 fw-bold">$<fmt:formatNumber value="${ingresosAnio}" pattern="#,##0.00"/></div>
        </div>
        <div class="cuadro-panel">
            <div class="text-secondary small">Membresías vigentes</div>
            <div class="fs-3 fw-bold"><c:out value="${vigentes}"/></div>
        </div>
    </div>
</div>

<div class="cuadro-panel">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="h5 fw-normal mb-0">Membresías de los socios</h2>
        <a href="${ctx}/membresias/nuevo" class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i> Nueva membresía</a>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead>
                <tr><th>ID</th><th>Socio</th><th>Plan</th><th>Inicio</th><th>Vencimiento</th><th>Precio</th><th>Estatus</th><th class="text-end">Acciones</th></tr>
            </thead>
            <tbody>
                <c:forEach items="${lista}" var="m">
                    <tr>
                        <td>#<c:out value="${m.id_mensualidad}"/></td>
                        <td><c:out value="${m.socio.nom_socio}"/></td>
                        <td><c:out value="${m.plan.nombre}"/></td>
                        <td><c:out value="${m.fecha_i}"/></td>
                        <td><c:out value="${m.fecha_f}"/></td>
                        <td>$<fmt:formatNumber value="${m.precio_cobrado}" pattern="#,##0.00"/></td>
                        <td><span class="badge ${m.estatus == 'VIGENTE' ? 'text-bg-success' : (m.estatus == 'VENCIDA' ? 'text-bg-danger' : 'text-bg-secondary')}"><c:out value="${m.estatus}"/></span></td>
                        <td class="text-end text-nowrap">
                            <a href="${ctx}/membresias/editar/${m.id_mensualidad}" class="btn btn-sm btn-outline-primary" title="Editar / renovar"><i class="bi bi-pencil"></i></a>
                            <button type="button" class="btn btn-sm btn-outline-danger" title="Eliminar"
                                    data-bs-toggle="modal" data-bs-target="#modalEliminar"
                                    data-url="${ctx}/membresias/eliminar/${m.id_mensualidad}"
                                    data-nombre="la membresía #${m.id_mensualidad}">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty lista}">
                    <tr><td colspan="8" class="text-center text-secondary py-4">Aún no hay membresías registradas</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="pie.jsp"/>
<jsp:include page="modal-eliminar.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    // datosParaJS lo arma el controlador: 12 valores (ene-dic) con los ingresos de cada mes
    const ingresos = ${empty datosParaJS ? '[0,0,0,0,0,0,0,0,0,0,0,0]' : datosParaJS};

    Chart.defaults.color = '#adb5bd';
    Chart.defaults.borderColor = 'rgba(255, 255, 255, 0.1)';

    new Chart(document.getElementById('graficoIngresos'), {
        type: 'line',
        data: {
            labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
            datasets: [{
                label: 'Ingresos',
                data: ingresos,
                borderColor: '#22c55e',
                backgroundColor: 'rgba(34, 197, 94, 0.2)',
                borderWidth: 2,
                pointBackgroundColor: '#22c55e',
                pointRadius: 4,
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: false } },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: { callback: function (v) { return '$' + v.toLocaleString('es-MX', { minimumFractionDigits: 2 }); } },
                    border: { display: false }
                },
                x: { grid: { display: false }, border: { display: false } }
            }
        }
    });
</script>
</body>
</html>
