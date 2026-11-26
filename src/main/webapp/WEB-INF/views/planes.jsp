<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="Planes de membresía"/>
    <jsp:param name="activo" value="planes"/>
    <jsp:param name="subtitulo" value="Catálogo de planes y tarifas (solo administrador)"/>
</jsp:include>

<div class="cuadro-panel">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="h5 fw-normal mb-0">Planes disponibles</h2>
        <a href="${ctx}/planes/nuevo" class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i> Nuevo plan</a>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead>
                <tr><th>ID</th><th>Nombre</th><th>Duración</th><th>Precio</th><th>Estatus</th><th class="text-end">Acciones</th></tr>
            </thead>
            <tbody>
                <c:forEach items="${lista}" var="p">
                    <tr>
                        <td>#<c:out value="${p.id_plan}"/></td>
                        <td><c:out value="${p.nombre}"/></td>
                        <td><c:out value="${p.duracion_dias}"/> días</td>
                        <td>$<fmt:formatNumber value="${p.precio}" pattern="#,##0.00"/></td>
                        <td><span class="badge ${p.activo ? 'text-bg-success' : 'text-bg-secondary'}">${p.activo ? 'ACTIVO' : 'INACTIVO'}</span></td>
                        <td class="text-end text-nowrap">
                            <a href="${ctx}/planes/editar/${p.id_plan}" class="btn btn-sm btn-outline-primary" title="Editar"><i class="bi bi-pencil"></i></a>
                            <button type="button" class="btn btn-sm btn-outline-danger" title="Eliminar"
                                    data-bs-toggle="modal" data-bs-target="#modalEliminar"
                                    data-url="${ctx}/planes/eliminar/${p.id_plan}"
                                    data-nombre="el plan <c:out value='${p.nombre}'/>">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty lista}">
                    <tr><td colspan="6" class="text-center text-secondary py-4">Aún no hay planes registrados</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
    <p class="small text-secondary mt-3 mb-0"><i class="bi bi-info-circle me-1"></i>Un plan con membresías asignadas no se puede eliminar; márcalo como INACTIVO para dejar de ofrecerlo.</p>
</div>

<jsp:include page="pie.jsp"/>
<jsp:include page="modal-eliminar.jsp"/>
</body>
</html>
