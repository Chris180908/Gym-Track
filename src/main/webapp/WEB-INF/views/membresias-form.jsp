<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="editando" value="${not empty dato.id_mensualidad}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="${editando ? 'Editar / renovar membresía' : 'Nueva membresía'}"/>
    <jsp:param name="activo" value="membresias"/>
    <jsp:param name="subtitulo" value="El plan define el precio y la duración; el vencimiento se calcula solo"/>
</jsp:include>

<form action="${ctx}/membresias/guardar" method="post" class="cuadro-panel">
    <input type="hidden" name="token" value="${sessionScope.token}">
    <c:if test="${editando}"><input type="hidden" name="id" value="${dato.id_mensualidad}"></c:if>

    <div class="row g-3">
        <div class="col-md-6">
            <label for="idSocio" class="form-label">Socio *</label>
            <select class="form-select" id="idSocio" name="idSocio" required>
                <option value="" disabled ${empty dato.socio ? 'selected' : ''}>Selecciona un socio…</option>
                <c:forEach items="${socios}" var="s">
                    <option value="${s.id_socio}" ${s.id_socio == dato.socio.id_socio ? 'selected' : ''}
                        ${(s.estatus != 'ACTIVO' and s.id_socio != dato.socio.id_socio) ? 'disabled' : ''}>
                        #<c:out value="${s.id_socio}"/> - <c:out value="${s.nom_socio}"/><c:if test="${s.estatus != 'ACTIVO'}"> [inactivo]</c:if>
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-6">
            <label for="idPlan" class="form-label">Plan *</label>
            <select class="form-select" id="idPlan" name="idPlan" required>
                <option value="" disabled ${empty dato.plan ? 'selected' : ''}>Selecciona un plan…</option>
                <c:forEach items="${planes}" var="p">
                    <option value="${p.id_plan}" ${p.id_plan == dato.plan.id_plan ? 'selected' : ''}
                            ${(not p.activo and p.id_plan != dato.plan.id_plan) ? 'disabled' : ''}>
                        <c:out value="${p.nombre}"/> - $<fmt:formatNumber value="${p.precio}" pattern="#,##0.00"/> (<c:out value="${p.duracion_dias}"/> días)<c:if test="${not p.activo}"> [inactivo]</c:if>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="col-md-4">
            <label for="fecha_i" class="form-label">Fecha de inicio *</label>
            <input type="date" class="form-control" id="fecha_i" name="fecha_i" value="${dato.fecha_i}" required>
        </div>
        <div class="col-md-4">
            <label for="estatus" class="form-label">Estatus</label>
            <select class="form-select" id="estatus" name="estatus">
                <option value="VIGENTE" ${empty dato.estatus or dato.estatus == 'VIGENTE' ? 'selected' : ''}>VIGENTE</option>
                <option value="VENCIDA" ${dato.estatus == 'VENCIDA' ? 'selected' : ''}>VENCIDA</option>
                <option value="CANCELADA" ${dato.estatus == 'CANCELADA' ? 'selected' : ''}>CANCELADA</option>
            </select>
        </div>

        <c:choose>
            <c:when test="${editando}">
                <div class="col-md-4">
                    <label class="form-label">Vencimiento actual</label>
                    <input type="text" class="form-control" value="${dato.fecha_f}" disabled>
                </div>
                <div class="col-md-4">
                    <label for="metodoPago" class="form-label">Cobrar renovación</label>
                    <select class="form-select" id="metodoPago" name="metodoPago">
                        <option value="">No registrar pago ahora</option>
                        <option value="EFECTIVO">EFECTIVO</option>
                        <option value="TARJETA">TARJETA</option>
                        <option value="TRANSFERENCIA">TRANSFERENCIA</option>
                    </select>
                </div>
                <div class="col-12">
                    <div class="alert alert-info mb-0 py-2">
                        <i class="bi bi-info-circle me-1"></i>
                        Para renovar, cambia la fecha de inicio (y el plan si hace falta). Si eliges un método de cobro, se registra el pago con el precio de la membresía.
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="col-md-4">
                    <label for="metodoPago" class="form-label">Método de pago *</label>
                    <select class="form-select" id="metodoPago" name="metodoPago" required>
                        <option value="EFECTIVO">EFECTIVO</option>
                        <option value="TARJETA">TARJETA</option>
                        <option value="TRANSFERENCIA">TRANSFERENCIA</option>
                    </select>
                </div>
                <div class="col-12">
                    <div class="alert alert-info mb-0 py-2">
                        <i class="bi bi-info-circle me-1"></i>
                        Al guardar se registra automáticamente el pago de la membresía con el método elegido.
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="d-flex justify-content-end gap-2 mt-4">
        <a href="${ctx}/membresias" class="btn btn-secondary">Cancelar</a>
        <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg me-1"></i> Guardar</button>
    </div>
</form>

<jsp:include page="pie.jsp"/>
</body>
</html>
