<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Modal de confirmación reutilizable. Los botones de eliminar mandan data-url y data-nombre. --%>
<div class="modal fade" id="modalEliminar" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form id="formEliminar" method="post">
                <input type="hidden" name="token" value="${sessionScope.token}">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="bi bi-trash me-2 text-danger"></i>Confirmar eliminación</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    ¿Seguro que quieres eliminar <strong id="nombreEliminar"></strong>? Esta acción no se puede deshacer.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-danger">Eliminar</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    document.getElementById('modalEliminar').addEventListener('show.bs.modal', function (e) {
        document.getElementById('formEliminar').action = e.relatedTarget.dataset.url;
        document.getElementById('nombreEliminar').textContent = e.relatedTarget.dataset.nombre;
    });
</script>
