<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gymtrack - Consultas y Reportes</title>
    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="estilos.css" rel="stylesheet">
    <style>
        @media print {
            .no-print { display: none !important; }
            body, .contenido-principal { background-color: #fff !important; color: #000 !important; height: auto !important; overflow: visible !important; }
            .card, .table { border: 1px solid #dee2e6 !important; color: #000 !important; }
        }
    </style>
</head>
<body>

<!-- Contenedor principal en fila (Sidebar a la izquierda + Contenido a la derecha) -->
<div class="d-flex">

    <div class="no-print">
        <jsp:include page="navbar.jsp" />
    </div>

    <main class="flex-grow-1 p-4 contenido-principal" style="height: 100vh; overflow-y: auto;">
        <div class="container-fluid">

            <!-- Encabezado y Acciones Principales -->
            <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 gap-2">
                <div>
                    <h2 class="fw-bold mb-0"><i class="bi bi-bar-chart-line-fill text-primary me-2"></i>Consultas y Reportes</h2>
                    <p class="text-secondary mb-0">Actividad del gimnasio: socios, membresías e ingresos</p>
                </div>
                <div class="d-flex gap-2 no-print">
                    <button type="button" class="btn btn-outline-light" onclick="window.print()">
                        <i class="bi bi-printer me-1"></i> Imprimir / PDF
                    </button>
                </div>
            </div>

            <!-- Tarjetas de Resumen (KPIs - 3 columnas) -->
            <div class="row g-3 mb-4">
                <div class="col-12 col-md-4">
                    <div class="cuadro-panel h-100 shadow-sm">
                        <div class="card-body d-flex align-items-center">
                            <div class="bg-primary bg-opacity-10 text-primary rounded-3 p-3 me-3">
                                <i class="bi bi-people-fill fs-3"></i>
                            </div>
                            <div>
                                <span class="text-secondary small d-block">Socios Activos</span>
                                <h4 class="fw-bold mb-0">${totalSocios != null ? totalSocios : '124'}</h4>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-md-4">
                    <div class="cuadro-panel h-100 shadow-sm">
                        <div class="card-body d-flex align-items-center">
                            <div class="bg-success bg-opacity-10 text-success rounded-3 p-3 me-3">
                                <i class="bi bi-card-checklist fs-3"></i>
                            </div>
                            <div>
                                <span class="text-secondary small d-block">Membresías Vigentes</span>
                                <h4 class="fw-bold mb-0">${membresiasVigentes != null ? membresiasVigentes : '98'}</h4>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-md-4">
                    <div class="cuadro-panel h-100 shadow-sm">
                        <div class="card-body d-flex align-items-center">
                            <div class="bg-warning bg-opacity-10 text-warning rounded-3 p-3 me-3">
                                <i class="bi bi-cash-coin fs-3"></i>
                            </div>
                            <div>
                                <span class="text-secondary small d-block">Ingresos del Periodo</span>
                                <h4 class="fw-bold mb-0">$${ingresosTotales != null ? ingresosTotales : '48,500.00'}</h4>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="cuadro-panel mb-4 shadow-sm no-print">
                <div class="card-header bg-body-tertiary fw-semibold">
                    <i class="bi bi-funnel me-1"></i> Filtros de Consulta y Generación de Reporte
                </div>
                <div class="cuadro-panel">
                    <form action="ReporteServlet" method="GET" class="row g-3 align-items-end">
                        <div class="col-md-3">
                            <label for="tipoReporte" class="form-label small text-secondary">Módulo / Tipo de Reporte</label>
                            <select class="form-select" id="tipoReporte" name="tipoReporte">
                                <option value="GENERAL">Actividad General</option>
                                <option value="SOCIOS">Socios</option>
                                <option value="MEMBRESIAS">Membresías</option>
                                <option value="PAGOS">Pagos e Ingresos</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label for="fechaInicio" class="form-label small text-secondary">Fecha Inicio</label>
                            <input type="date" class="form-control" id="fechaInicio" name="fechaInicio">
                        </div>
                        <div class="col-md-3">
                            <label for="fechaFin" class="form-label small text-secondary">Fecha Fin</label>
                            <input type="date" class="form-control" id="fechaFin" name="fechaFin">
                        </div>
                        <div class="col-md-3 d-flex gap-2">
                            <button type="submit" name="accion" value="consultar" class="btn btn-primary w-100">
                                <i class="bi bi-search me-1"></i> Consultar
                            </button>
                            <button type="submit" name="accion" value="guardarReporte" class="btn btn-success w-100" title="Guarda el registro en la tabla REPORTE">
                                <i class="bi bi-file-earmark-plus me-1"></i> Generar
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="cuadro-panel shadow-sm">
                <div class="card-header bg-body-tertiary no-print">
                    <ul class="nav nav-tabs card-header-tabs" id="reportesTabs" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="pagos-tab" data-bs-toggle="tab" data-bs-target="#pagos-pane" type="button" role="tab">
                                <i class="bi bi-credit-card me-1"></i> Pagos
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="membresias-tab" data-bs-toggle="tab" data-bs-target="#membresias-pane" type="button" role="tab">
                                <i class="bi bi-person-badge me-1"></i> Membresías
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="socios-tab" data-bs-toggle="tab" data-bs-target="#socios-pane" type="button" role="tab">
                                <i class="bi bi-people me-1"></i> Socios
                            </button>
                        </li>
                        <li class="nav-item ms-auto" role="presentation">
                            <button class="nav-link text-warning" id="historial-tab" data-bs-toggle="tab" data-bs-target="#historial-pane" type="button" role="tab">
                                <i class="bi bi-archive me-1"></i> Historial de Reportes
                            </button>
                        </li>
                    </ul>
                </div>

                <div class="card-body">
                    <!-- Barra de búsqueda rápida en cliente -->
                    <div class="row mb-3 no-print">
                        <div class="col-md-4 ms-auto">
                            <div class="input-group input-group-sm">
                                <span class="input-group-text"><i class="bi bi-search"></i></span>
                                <input type="text" id="buscadorTabla" class="form-control" placeholder="Filtrar resultados en pantalla...">
                            </div>
                        </div>
                    </div>

                    <div class="tab-content" id="reportesTabsContent">

                        <!-- 1. TAB PAGOS -->
                        <div class="tab-pane fade show active" id="pagos-pane" role="tabpanel">
                            <h5 class="mb-3">Detalle de Pagos</h5>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>ID Pago</th>
                                            <th>Socio</th>
                                            <th>Concepto</th>
                                            <th>Método</th>
                                            <th>Fecha</th>
                                            <th>Monto</th>
                                            <th>Recepcionista</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>#101</td>
                                            <td>Carlos Mendoza</td>
                                            <td>Mensualidad Noviembre</td>
                                            <td><span class="badge bg-info text-dark">TARJETA</span></td>
                                            <td>2026-11-18 10:30</td>
                                            <td class="text-success fw-bold">$550.00</td>
                                            <td>Ana López</td>
                                        </tr>
                                        <tr>
                                            <td>#102</td>
                                            <td>Sofía Ramírez</td>
                                            <td>Plan Trimestral</td>
                                            <td><span class="badge bg-success">EFECTIVO</span></td>
                                            <td>2026-11-19 08:15</td>
                                            <td class="text-success fw-bold">$1,450.00</td>
                                            <td>Ana López</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- 2. TAB MEMBRESIAS -->
                        <div class="tab-pane fade" id="membresias-pane" role="tabpanel">
                            <h5 class="mb-3">Estado de Membresías</h5>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>ID</th>
                                            <th>Socio</th>
                                            <th>Plan</th>
                                            <th>Inicio</th>
                                            <th>Vencimiento</th>
                                            <th>Precio</th>
                                            <th>Estatus</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>#45</td>
                                            <td>Carlos Mendoza</td>
                                            <td>Mensual</td>
                                            <td>2026-11-01</td>
                                            <td>2026-12-01</td>
                                            <td>$550.00</td>
                                            <td><span class="badge bg-success">VIGENTE</span></td>
                                        </tr>
                                        <tr>
                                            <td>#46</td>
                                            <td>Luis Torres</td>
                                            <td>Mensual</td>
                                            <td>2026-10-10</td>
                                            <td>2026-11-10</td>
                                            <td>$550.00</td>
                                            <td><span class="badge bg-danger">VENCIDA</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- 3. TAB SOCIOS -->
                        <div class="tab-pane fade" id="socios-pane" role="tabpanel">
                            <h5 class="mb-3">Directorio de Socios</h5>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre</th>
                                            <th>Teléfono</th>
                                            <th>Correo</th>
                                            <th>Dirección</th>
                                            <th>Estatus</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>1</td>
                                            <td>Carlos Mendoza</td>
                                            <td>5512345678</td>
                                            <td>carlos@correo.com</td>
                                            <td>Col. Centro, CP 06000</td>
                                            <td><span class="badge bg-success">ACTIVO</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- 4. TAB HISTORIAL DE REPORTES GENERADOS -->
                        <div class="tab-pane fade" id="historial-pane" role="tabpanel">
                            <h5 class="mb-3">Bitácora de Reportes Generados</h5>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>ID Reporte</th>
                                            <th>Tipo</th>
                                            <th>Periodo Consultado</th>
                                            <th>Fecha de Generación</th>
                                            <th>Generado Por</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>#12</td>
                                            <td>PAGOS</td>
                                            <td>2026-11-01 al 2026-11-19</td>
                                            <td>2026-11-19 11:00:00</td>
                                            <td>Ana López</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

        </div>
    </main>

</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Filtro rápido en tiempo real para las tablas -->
<script>
    document.getElementById('buscadorTabla').addEventListener('keyup', function () {
        const texto = this.value.toLowerCase();
        const filas = document.querySelectorAll('.tab-pane.active tbody tr');
        filas.forEach(fila => {
            fila.style.display = fila.textContent.toLowerCase().includes(texto) ? '' : 'none';
        });
    });
</script>

</body>
</html>