<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="editando" value="${not empty dato.id_socio}"/>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="${editando ? 'Editar socio' : 'Nuevo socio'}"/>
    <jsp:param name="activo" value="socios"/>
    <jsp:param name="subtitulo" value="Datos de contacto y acceso del socio"/>
</jsp:include>

<form action="${ctx}/socios/guardar" method="post" class="cuadro-panel">
    <input type="hidden" name="token" value="${sessionScope.token}">
    <c:if test="${editando}"><input type="hidden" name="id" value="${dato.id_socio}"></c:if>

    <div class="row g-3">
        <div class="col-md-6">
            <label for="nom_socio" class="form-label">Nombre completo *</label>
            <input type="text" class="form-control" id="nom_socio" name="nom_socio" maxlength="100" value="<c:out value='${dato.nom_socio}'/>" required>
        </div>
        <div class="col-md-6">
            <label for="correo_socio" class="form-label">Correo electrónico *</label>
            <input type="email" class="form-control" id="correo_socio" name="correo_socio" maxlength="100" value="<c:out value='${dato.correo_socio}'/>" required>
        </div>
        <div class="col-md-6">
            <label for="contra_socio" class="form-label">Contraseña <c:if test="${not editando}">*</c:if></label>
            <input type="password" class="form-control" id="contra_socio" name="contra_socio" maxlength="255" autocomplete="new-password"
                   placeholder="${editando ? 'Déjala vacía para conservar la actual' : ''}" ${editando ? '' : 'required'}>
        </div>
        <div class="col-md-3">
            <label for="tel_socio" class="form-label">Teléfono</label>
            <input type="tel" class="form-control" id="tel_socio" name="tel_socio" maxlength="15" value="<c:out value='${dato.tel_socio}'/>">
        </div>
        <div class="col-md-3">
            <label for="estatus" class="form-label">Estatus</label>
            <select class="form-select" id="estatus" name="estatus">
                <option value="ACTIVO" ${dato.estatus != 'INACTIVO' ? 'selected' : ''}>ACTIVO</option>
                <option value="INACTIVO" ${dato.estatus == 'INACTIVO' ? 'selected' : ''}>INACTIVO</option>
            </select>
        </div>

        <div class="col-12"><hr class="my-1"><span class="text-secondary small">Dirección</span></div>
        <div class="col-md-4">
            <label for="num_calle_s" class="form-label">Calle y número</label>
            <input type="text" class="form-control" id="num_calle_s" name="num_calle_s" maxlength="100" value="<c:out value='${dato.num_calle_s}'/>">
        </div>
        <div class="col-md-3">
            <label for="col_s" class="form-label">Colonia</label>
            <input type="text" class="form-control" id="col_s" name="col_s" maxlength="100" value="<c:out value='${dato.col_s}'/>">
        </div>
        <div class="col-md-3">
            <label for="edo_s" class="form-label">Estado</label>
            <input type="text" class="form-control" id="edo_s" name="edo_s" maxlength="100" value="<c:out value='${dato.edo_s}'/>">
        </div>
        <div class="col-md-2">
            <label for="cod_post_s" class="form-label">C.P.</label>
            <input type="text" class="form-control" id="cod_post_s" name="cod_post_s" maxlength="10" value="<c:out value='${dato.cod_post_s}'/>">
        </div>
    </div>

    <div class="d-flex justify-content-end gap-2 mt-4">
        <a href="${ctx}/socios" class="btn btn-secondary">Cancelar</a>
        <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg me-1"></i> Guardar</button>
    </div>
</form>

<jsp:include page="pie.jsp"/>
</body>
</html>
