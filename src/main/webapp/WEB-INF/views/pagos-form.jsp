<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="editando" value="${not empty dato.id_pago}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="${editando ? 'Editar pago' : 'Registrar pago'}"/>
    <jsp:param name="activo" value="pagos"/>
    <jsp:param name="subtitulo" value="La fecha y quién cobró se guardan automáticamente"/>
</jsp:include>

<form action="${ctx}/pagos/guardar" method="post" class="cuadro-panel">
    <input type="hidden" name="token" value="${sessionScope.token}">
    <c:if test="${editando}"><input type="hidden" name="id" value="${dato.id_pago}"></c:if>

    <div class="row g-3">
        <div class="col-md-12">
            <label for="idMembresia" class="form-label">Membresía *</label>
            <select class="form-select" id="idMembresia" name="idMembresia" required>
                <option value="" disabled ${empty dato.membresia ? 'selected' : ''}>Selecciona una membresía…</option>
                <c:forEach items="${membresias}" var="m">
                    <option value="${m.id_mensualidad}" ${m.id_mensualidad == dato.membresia.id_mensualidad ? 'selected' : ''}>
                        #<c:out value="${m.id_mensualidad}"/> - <c:out value="${m.socio.nom_socio}"/> - <c:out value="${m.plan.nombre}"/> (<c:out value="${m.fecha_i}"/> a <c:out value="${m.fecha_f}"/>)
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-4">
            <label for="monto" class="form-label">Monto *</label>
            <div class="input-group">
                <span class="input-group-text">$</span>
                <input type="number" step="0.01" min="0.01" class="form-control" id="monto" name="monto" value="${dato.monto}" required>
            </div>
        </div>
        <div class="col-md-4">
            <label for="metodo_pago" class="form-label">Método de pago *</label>
            <select class="form-select" id="metodo_pago" name="metodo_pago" required>
                <option value="EFECTIVO" ${empty dato.metodo_pago or dato.metodo_pago == 'EFECTIVO' ? 'selected' : ''}>EFECTIVO</option>
                <option value="TARJETA" ${dato.metodo_pago == 'TARJETA' ? 'selected' : ''}>TARJETA</option>
                <option value="TRANSFERENCIA" ${dato.metodo_pago == 'TRANSFERENCIA' ? 'selected' : ''}>TRANSFERENCIA</option>
            </select>
        </div>
        <div class="col-md-4">
            <label for="concepto" class="form-label">Concepto *</label>
            <input type="text" class="form-control" id="concepto" name="concepto" maxlength="100" value="<c:out value='${dato.concepto}'/>" placeholder="Ej. Mensualidad noviembre" required>
        </div>
    </div>

    <div class="d-flex justify-content-end gap-2 mt-4">
        <a href="${ctx}/pagos" class="btn btn-secondary">Cancelar</a>
        <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg me-1"></i> Guardar</button>
    </div>
</form>

<jsp:include page="pie.jsp"/>
</body>
</html>
