<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="editando" value="${not empty dato.id_recepcion}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="${editando ? 'Editar recepcionista' : 'Nuevo recepcionista'}"/>
    <jsp:param name="activo" value="recepcionistas"/>
    <jsp:param name="subtitulo" value="Cuenta con la que el empleado entrará a GymTrack"/>
</jsp:include>

<form action="${ctx}/recepcionistas/guardar" method="post" class="cuadro-panel">
    <input type="hidden" name="token" value="${sessionScope.token}">
    <c:if test="${editando}"><input type="hidden" name="id" value="${dato.id_recepcion}"></c:if>

    <div class="row g-3">
        <div class="col-md-6">
            <label for="nom_recepcion" class="form-label">Nombre completo *</label>
            <input type="text" class="form-control" id="nom_recepcion" name="nom_recepcion" maxlength="100" value="<c:out value='${dato.nom_recepcion}'/>" required>
        </div>
        <div class="col-md-6">
            <label for="correo_recepcion" class="form-label">Correo electrónico *</label>
            <input type="email" class="form-control" id="correo_recepcion" name="correo_recepcion" maxlength="100" value="<c:out value='${dato.correo_recepcion}'/>" required>
        </div>
        <div class="col-md-6">
            <label for="contra_recepcion" class="form-label">Contraseña <c:if test="${not editando}">*</c:if></label>
            <input type="password" class="form-control" id="contra_recepcion" name="contra_recepcion" maxlength="255" autocomplete="new-password"
                   placeholder="${editando ? 'Déjala vacía para conservar la actual' : ''}" ${editando ? '' : 'required'}>
        </div>
        <div class="col-md-6">
            <label for="tel_recepcion" class="form-label">Teléfono</label>
            <input type="tel" class="form-control" id="tel_recepcion" name="tel_recepcion" maxlength="15" value="<c:out value='${dato.tel_recepcion}'/>">
        </div>

        <div class="col-12"><hr class="my-1"><span class="text-secondary small">Dirección</span></div>
        <div class="col-md-4">
            <label for="num_calle_r" class="form-label">Calle y número</label>
            <input type="text" class="form-control" id="num_calle_r" name="num_calle_r" maxlength="100" value="<c:out value='${dato.num_calle_r}'/>">
        </div>
        <div class="col-md-3">
            <label for="col_r" class="form-label">Colonia</label>
            <input type="text" class="form-control" id="col_r" name="col_r" maxlength="100" value="<c:out value='${dato.col_r}'/>">
        </div>
        <div class="col-md-3">
            <label for="edo_r" class="form-label">Estado</label>
            <input type="text" class="form-control" id="edo_r" name="edo_r" maxlength="100" value="<c:out value='${dato.edo_r}'/>">
        </div>
        <div class="col-md-2">
            <label for="cod_post_r" class="form-label">C.P.</label>
            <input type="text" class="form-control" id="cod_post_r" name="cod_post_r" maxlength="10" value="<c:out value='${dato.cod_post_r}'/>">
        </div>
    </div>

    <div class="d-flex justify-content-end gap-2 mt-4">
        <a href="${ctx}/recepcionistas" class="btn btn-secondary">Cancelar</a>
        <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg me-1"></i> Guardar</button>
    </div>
</form>

<jsp:include page="pie.jsp"/>
</body>
</html>
