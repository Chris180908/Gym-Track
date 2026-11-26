<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="partes" value="${fn:split(dato.nom_recepcion, ' ')}"/>
<c:set var="esAdmin" value="${sessionScope.rol == 'ADMIN'}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="Perfil"/>
    <jsp:param name="activo" value="perfil"/>
    <jsp:param name="subtitulo" value=""/>
</jsp:include>

<div class="row g-4">
    <%-- Tarjeta con iniciales --%>
    <div class="col-12 col-xl-4">
        <div class="cuadro-panel h-100 text-center">
            <div class="d-flex justify-content-center mb-3">
                <div class="avatar-iniciales d-flex align-items-center justify-content-center">
                    <c:out value="${fn:toUpperCase(fn:substring(partes[0], 0, 1))}${fn:toUpperCase(fn:substring(partes[1], 0, 1))}"/>
                </div>
            </div>
            <h2 class="h5 mb-1"><c:out value="${dato.nom_recepcion}"/></h2>
            <span class="badge text-bg-primary">${esAdmin ? 'Administrador' : 'Recepcionista'}</span>
        </div>
    </div>

    <%-- Datos --%>
    <div class="col-12 col-xl-8">
        <div class="cuadro-panel h-100">
            <h2 class="h5 fw-normal mb-4">Datos del usuario</h2>
            <div class="row">
                <div class="col-12 col-md-6">
                    <div class="dato-etiqueta"><i class="bi bi-hash me-1"></i>ID</div>
                    <div class="dato-valor"><c:out value="${dato.id_recepcion}"/></div>
                </div>
                <div class="col-12 col-md-6">
                    <div class="dato-etiqueta"><i class="bi bi-person me-1"></i>Nombre</div>
                    <div class="dato-valor"><c:out value="${dato.nom_recepcion}"/></div>
                </div>
                <div class="col-12 col-md-6">
                    <div class="dato-etiqueta"><i class="bi bi-telephone me-1"></i>Teléfono</div>
                    <div class="dato-valor"><c:out value="${empty dato.tel_recepcion ? '—' : dato.tel_recepcion}"/></div>
                </div>
                <div class="col-12 col-md-6">
                    <div class="dato-etiqueta"><i class="bi bi-envelope me-1"></i>Correo</div>
                    <div class="dato-valor"><c:out value="${dato.correo_recepcion}"/></div>
                </div>
                <div class="col-12">
                    <div class="dato-etiqueta"><i class="bi bi-geo-alt me-1"></i>Dirección</div>
                    <div class="dato-valor">
                        <c:out value="${empty dato.num_calle_r and empty dato.col_r ? '—' : dato.num_calle_r}"/>
                        <c:if test="${not empty dato.col_r}">, <c:out value="${dato.col_r}"/></c:if>
                        <c:if test="${not empty dato.edo_r}">, <c:out value="${dato.edo_r}"/></c:if>
                        <c:if test="${not empty dato.cod_post_r}">, C.P. <c:out value="${dato.cod_post_r}"/></c:if>
                    </div>
                </div>
            </div>
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalPerfil">
                <i class="bi bi-pencil me-1"></i> Editar perfil
            </button>
        </div>
    </div>
</div>

<%-- Modal para editar el perfil --%>
<div class="modal fade" id="modalPerfil" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <form action="${ctx}/perfil" method="post">
                <input type="hidden" name="token" value="${sessionScope.token}">
                <div class="modal-header">
                    <h5 class="modal-title">Editar perfil</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">

                        <%-- Nombre y correo: solo los puede cambiar el ADMIN --%>
                        <c:if test="${esAdmin}">
                            <div class="col-md-6">
                                <label for="nom_recepcion" class="form-label">Nombre completo *</label>
                                <input type="text" class="form-control" id="nom_recepcion" name="nom_recepcion" maxlength="100" value="<c:out value='${dato.nom_recepcion}'/>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="correo_recepcion" class="form-label">Correo electrónico *</label>
                                <input type="email" class="form-control" id="correo_recepcion" name="correo_recepcion" maxlength="100" value="<c:out value='${dato.correo_recepcion}'/>" required>
                            </div>
                        </c:if>

                        <div class="col-md-6">
                            <label for="tel_recepcion" class="form-label">Teléfono</label>
                            <input type="tel" class="form-control" id="tel_recepcion" name="tel_recepcion" maxlength="15" value="<c:out value='${dato.tel_recepcion}'/>">
                        </div>
                        <div class="col-md-6">
                            <label for="num_calle_r" class="form-label">Calle y número</label>
                            <input type="text" class="form-control" id="num_calle_r" name="num_calle_r" maxlength="100" value="<c:out value='${dato.num_calle_r}'/>">
                        </div>
                        <div class="col-md-4">
                            <label for="col_r" class="form-label">Colonia</label>
                            <input type="text" class="form-control" id="col_r" name="col_r" maxlength="100" value="<c:out value='${dato.col_r}'/>">
                        </div>
                        <div class="col-md-4">
                            <label for="edo_r" class="form-label">Estado</label>
                            <input type="text" class="form-control" id="edo_r" name="edo_r" maxlength="100" value="<c:out value='${dato.edo_r}'/>">
                        </div>
                        <div class="col-md-4">
                            <label for="cod_post_r" class="form-label">C.P.</label>
                            <input type="text" class="form-control" id="cod_post_r" name="cod_post_r" maxlength="10" value="<c:out value='${dato.cod_post_r}'/>">
                        </div>
                    </div>

                    <c:if test="${not esAdmin}">
                        <p class="small text-secondary mt-3 mb-0">El nombre y el correo los administra el dueño del gimnasio.</p>
                    </c:if>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Guardar cambios</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="pie.jsp"/>
</body>
</html>
