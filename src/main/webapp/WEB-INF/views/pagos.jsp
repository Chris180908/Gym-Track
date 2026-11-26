<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="Pagos"/>
    <jsp:param name="activo" value="pagos"/>
    <jsp:param name="subtitulo" value="Cobros de membresías en efectivo, tarjeta o transferencia"/>
</jsp:include>

<div class="cuadro-panel">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="h5 fw-normal mb-0">Historial de pagos</h2>
        <a href="${ctx}/pagos/nuevo" class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i> Registrar pago</a>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead>
                <tr><th>ID</th><th>Fecha</th><th>Socio</th><th>Membresía</th><th>Concepto</th><th>Método</th><th>Monto</th><th>Registró</th><th class="text-end">Acciones</th></tr>
            </thead>
            <tbody>
                <c:forEach items="${lista}" var="p">
                    <tr>
                        <td>#<c:out value="${p.id_pago}"/></td>
                        <td><c:out value="${fn:replace(fn:substring(p.fecha_pago, 0, 16), 'T', ' ')}"/></td>
                        <td><c:out value="${p.membresia.socio.nom_socio}"/></td>
                        <td>#<c:out value="${p.membresia.id_mensualidad}"/> · <c:out value="${p.membresia.plan.nombre}"/></td>
                        <td><c:out value="${p.concepto}"/></td>
                        <td><span class="badge ${p.metodo_pago == 'EFECTIVO' ? 'text-bg-success' : (p.metodo_pago == 'TARJETA' ? 'text-bg-info' : 'text-bg-secondary')}"><c:out value="${p.metodo_pago}"/></span></td>
                        <td class="text-success fw-bold">$<fmt:formatNumber value="${p.monto}" pattern="#,##0.00"/></td>
                        <td><c:out value="${p.recepcionista.nom_recepcion}"/></td>
                        <td class="text-end text-nowrap">
                            <a href="${ctx}/pagos/editar/${p.id_pago}" class="btn btn-sm btn-outline-primary" title="Editar"><i class="bi bi-pencil"></i></a>
                            <button type="button" class="btn btn-sm btn-outline-danger" title="Eliminar"
                                    data-bs-toggle="modal" data-bs-target="#modalEliminar"
                                    data-url="${ctx}/pagos/eliminar/${p.id_pago}"
                                    data-nombre="el pago #${p.id_pago}">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty lista}">
                    <tr><td colspan="9" class="text-center text-secondary py-4">Aún no hay pagos registrados</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="pie.jsp"/>
<jsp:include page="modal-eliminar.jsp"/>
</body>
</html>
