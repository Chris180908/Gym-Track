<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="Socios"/>
    <jsp:param name="activo" value="socios"/>
    <jsp:param name="subtitulo" value="Alta, consulta, cambios y baja de socios"/>
</jsp:include>

<div class="cuadro-panel">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="h5 fw-normal mb-0">Listado de socios</h2>
        <a href="${ctx}/socios/nuevo" class="btn btn-primary"><i class="bi bi-plus-lg me-1"></i> Nuevo socio</a>
    </div>

    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead>
                <tr><th>ID</th><th>Nombre</th><th>Contacto</th><th>Dirección</th><th>Estatus</th><th class="text-end">Acciones</th></tr>
            </thead>
            <tbody>
                <c:forEach items="${lista}" var="s">
                    <tr>
                        <td>#<c:out value="${s.id_socio}"/></td>
                        <td><c:out value="${s.nom_socio}"/></td>
                        <td>
                            <div><i class="bi bi-envelope me-1 text-secondary"></i><c:out value="${s.correo_socio}"/></div>
                            <div class="small text-secondary"><i class="bi bi-telephone me-1"></i><c:out value="${s.tel_socio}"/></div>
                        </td>
                        <td><c:out value="${s.num_calle_s} ${s.col_s} ${s.edo_s} ${s.cod_post_s}"/></td>
                        <td><span class="badge ${s.estatus == 'ACTIVO' ? 'text-bg-success' : 'text-bg-secondary'}"><c:out value="${s.estatus}"/></span></td>
                        <td class="text-end text-nowrap">
                            <a href="${ctx}/socios/editar/${s.id_socio}" class="btn btn-sm btn-outline-primary" title="Editar"><i class="bi bi-pencil"></i></a>
                            <button type="button" class="btn btn-sm btn-outline-danger" title="Eliminar"
                                    data-bs-toggle="modal" data-bs-target="#modalEliminar"
                                    data-url="${ctx}/socios/eliminar/${s.id_socio}"
                                    data-nombre="<c:out value='${s.nom_socio}'/>">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty lista}">
                    <tr><td colspan="6" class="text-center text-secondary py-4">Aún no hay socios registrados</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
    <p class="small text-secondary mt-3 mb-0"><i class="bi bi-info-circle me-1"></i>Para eliminar un socio primero debe estar INACTIVO y sin membresía vigente.</p>
</div>

<jsp:include page="pie.jsp"/>
<jsp:include page="modal-eliminar.jsp"/>
</body>
</html>
