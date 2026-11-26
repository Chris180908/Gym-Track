<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="Recepcionistas"/>
    <jsp:param name="activo" value="recepcionistas"/>
    <jsp:param name="subtitulo" value="Cuentas del personal de recepción (solo administrador)"/>
</jsp:include>

<div class="cuadro-panel">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="h5 fw-normal mb-0">Personal registrado</h2>
        <a href="${ctx}/recepcionistas/nuevo" class="btn btn-primary"><i class="bi bi-person-plus me-1"></i> Nuevo recepcionista</a>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead>
                <tr><th>ID</th><th>Nombre</th><th>Contacto</th><th>Dirección</th><th class="text-end">Acciones</th></tr>
            </thead>
            <tbody>
                <c:forEach items="${lista}" var="r">
                    <tr>
                        <td>#<c:out value="${r.id_recepcion}"/></td>
                        <td><c:out value="${r.nom_recepcion}"/></td>
                        <td>
                            <div><i class="bi bi-envelope me-1 text-secondary"></i><c:out value="${r.correo_recepcion}"/></div>
                            <div class="small text-secondary"><i class="bi bi-telephone me-1"></i><c:out value="${r.tel_recepcion}"/></div>
                        </td>
                        <td><c:out value="${r.num_calle_r} ${r.col_r} ${r.edo_r} ${r.cod_post_r}"/></td>
                        <td class="text-end text-nowrap">
                            <a href="${ctx}/recepcionistas/editar/${r.id_recepcion}" class="btn btn-sm btn-outline-primary" title="Editar"><i class="bi bi-pencil"></i></a>
                            <button type="button" class="btn btn-sm btn-outline-danger" title="Eliminar"
                                    data-bs-toggle="modal" data-bs-target="#modalEliminar"
                                    data-url="${ctx}/recepcionistas/eliminar/${r.id_recepcion}"
                                    data-nombre="a <c:out value='${r.nom_recepcion}'/>">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty lista}">
                    <tr><td colspan="5" class="text-center text-secondary py-4">Aún no hay recepcionistas registrados</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
    <p class="small text-secondary mt-3 mb-0"><i class="bi bi-info-circle me-1"></i>Un recepcionista con socios, pagos o reportes asociados no se puede eliminar para conservar el historial.</p>
</div>

<jsp:include page="pie.jsp"/>
<jsp:include page="modal-eliminar.jsp"/>
</body>
</html>
