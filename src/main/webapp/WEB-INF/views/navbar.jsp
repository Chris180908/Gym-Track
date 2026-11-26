<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<aside class="d-flex flex-column flex-shrink-0 p-3 bg-body-tertiary position-sticky top-0 vh-100 overflow-y-auto no-print" style="width: 280px;">
    <a href="${ctx}/inicio" class="d-flex align-items-center mb-3 link-body-emphasis text-decoration-none">
        <span class="fs-4">GYMTRACK</span>
    </a>
    <hr class="mt-0">
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item mb-1">
            <a href="${ctx}/inicio" class="nav-link ${param.activo == 'inicio' ? 'active' : 'link-body-emphasis'}">
                <i class="bi bi-house-door me-2"></i> Inicio
            </a>
        </li>
        <li class="nav-item mb-1">
            <a href="${ctx}/socios" class="nav-link ${param.activo == 'socios' ? 'active' : 'link-body-emphasis'}">
                <i class="bi bi-people me-2"></i> Socios
            </a>
        </li>
        <li class="nav-item mb-1">
            <a href="${ctx}/membresias" class="nav-link ${param.activo == 'membresias' ? 'active' : 'link-body-emphasis'}">
                <i class="bi bi-person-vcard me-2"></i> Membresías
            </a>
        </li>
        <li class="nav-item mb-1">
            <a href="${ctx}/pagos" class="nav-link ${param.activo == 'pagos' ? 'active' : 'link-body-emphasis'}">
                <i class="bi bi-cash-coin me-2"></i> Pagos
            </a>
        </li>
        <li class="nav-item mb-1">
            <a href="${ctx}/reportes" class="nav-link ${param.activo == 'reportes' ? 'active' : 'link-body-emphasis'}">
                <i class="bi bi-grid me-2"></i> Reportes
            </a>
        </li>
        <li class="nav-item mb-1">
            <a href="${ctx}/perfil" class="nav-link ${param.activo == 'perfil' ? 'active' : 'link-body-emphasis'}">
                <i class="bi bi-person me-2"></i> Perfil
            </a>
        </li>

        <%-- Solo el dueño (ADMIN) ve esta sección --%>
        <c:if test="${sessionScope.rol == 'ADMIN'}">
            <li class="nav-item text-uppercase small text-secondary px-3 mt-3 mb-1">Administración</li>
            <li class="nav-item mb-1">
                <a href="${ctx}/planes" class="nav-link ${param.activo == 'planes' ? 'active' : 'link-body-emphasis'}">
                    <i class="bi bi-tags me-2"></i> Planes
                </a>
            </li>
            <li class="nav-item mb-1">
                <a href="${ctx}/recepcionistas" class="nav-link ${param.activo == 'recepcionistas' ? 'active' : 'link-body-emphasis'}">
                    <i class="bi bi-person-badge me-2"></i> Recepcionistas
                </a>
            </li>
            <li class="nav-item mb-1">
                <a href="${ctx}/roles" class="nav-link ${param.activo == 'roles' ? 'active' : 'link-body-emphasis'}">
                    <i class="bi bi-shield-lock me-2"></i> Roles
                </a>
            </li>
        </c:if>
    </ul>
    <hr>
    <div class="small text-secondary mb-2 text-truncate">
        <i class="bi bi-person-circle me-1"></i>
        <c:out value="${empty sessionScope.nombre ? sessionScope.rol : sessionScope.nombre}"/>
    </div>
    <form action="${ctx}/salir" method="post">
        <input type="hidden" name="token" value="${sessionScope.token}">
        <button type="submit" class="btn btn-outline-danger btn-sm w-100">
            <i class="bi bi-box-arrow-right me-1"></i> Cerrar sesión
        </button>
    </form>
</aside>
