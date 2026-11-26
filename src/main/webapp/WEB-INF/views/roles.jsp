<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="cabecera.jsp">
    <jsp:param name="titulo" value="Roles"/>
    <jsp:param name="activo" value="roles"/>
    <jsp:param name="subtitulo" value="Catálogo fijo de roles del sistema (solo consulta)"/>
</jsp:include>

<div class="cuadro-panel">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead><tr><th>ID</th><th>Nombre</th><th>Descripción</th></tr></thead>
            <tbody>
                <c:forEach items="${lista}" var="r">
                    <tr>
                        <td>#<c:out value="${r.id_rol}"/></td>
                        <td><span class="badge text-bg-primary"><c:out value="${r.nombre}"/></span></td>
                        <td><c:out value="${r.descripcion}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="pie.jsp"/>
</body>
</html>
