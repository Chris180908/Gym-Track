<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="editando" value="${not empty dato.id_plan}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="${editando ? 'Editar plan' : 'Nuevo plan'}"/>
    <jsp:param name="activo" value="planes"/>
    <jsp:param name="subtitulo" value="Cambiar el precio no modifica lo ya cobrado en membresías anteriores"/>
</jsp:include>

<form action="${ctx}/planes/guardar" method="post" class="cuadro-panel">
    <input type="hidden" name="token" value="${sessionScope.token}">
    <c:if test="${editando}"><input type="hidden" name="id" value="${dato.id_plan}"></c:if>

    <div class="row g-3">
        <div class="col-md-6">
            <label for="nombre" class="form-label">Nombre del plan *</label>
            <input type="text" class="form-control" id="nombre" name="nombre" maxlength="50" value="<c:out value='${dato.nombre}'/>" placeholder="Ej. Mensual" required>
        </div>
        <div class="col-md-2">
            <label for="duracion_dias" class="form-label">Duración (días) *</label>
            <input type="number" min="1" class="form-control" id="duracion_dias" name="duracion_dias" value="${dato.duracion_dias}" required>
        </div>
        <div class="col-md-2">
            <label for="precio" class="form-label">Precio *</label>
            <div class="input-group">
                <span class="input-group-text">$</span>
                <input type="number" min="0" step="0.01" class="form-control" id="precio" name="precio" value="${dato.precio}" required>
            </div>
        </div>
        <div class="col-md-2">
            <label for="activo" class="form-label">Estatus</label>
            <select class="form-select" id="activo" name="activo">
                <option value="true" ${empty dato.activo or dato.activo ? 'selected' : ''}>ACTIVO</option>
                <option value="false" ${not empty dato.activo and not dato.activo ? 'selected' : ''}>INACTIVO</option>
            </select>
        </div>
    </div>

    <div class="d-flex justify-content-end gap-2 mt-4">
        <a href="${ctx}/planes" class="btn btn-secondary">Cancelar</a>
        <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg me-1"></i> Guardar</button>
    </div>
</form>

<jsp:include page="pie.jsp"/>
</body>
</html>
