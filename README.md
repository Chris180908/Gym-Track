    # GymTrack — proyecto escolar Spring Boot + JSP

## Qué se completó
- Se eliminaron Usuario, UsuarioRepository, UsuarioService y UsuarioServiceImpl. Login conserva el DTO Login y usa RecepcionistaService.findByLogin.
- Las siete tablas de sql/base.sql tienen model, repository, service y ServiceImpl. Los identificadores son Integer, como los atributos y los INT de MySQL.
- Los controladores usan @Controller, Model, @ModelAttribute, @RequestParam, @GetMapping y @PostMapping para recibir formularios HTML y devolver JSP. Se conserva el estilo del ejemplo de Escuela: JpaRepository, List, Optional, @RequiredArgsConstructor y @Transactional. No se añadieron dependencias, JWT ni Spring Security.
- Admin y recepcionista son cuentas de RECEPCIONISTA; se distinguen mediante el ID_ROL. Los socios son registros de clientes y conservan su rol SOCIO, pero no tienen acceso a este panel.
- El rol ADMIN puede realizar las operaciones comunes y administrar recepcionistas. El rol RECEPCIONISTA no puede acceder a las rutas de personal ni al catálogo de roles. Se revisa la sesión y el rol en el servidor; los JSP quedan en WEB-INF/views para que no puedan abrirse directamente.
- Los roles son un catálogo fijo de permisos. RolService tiene los cinco métodos del ejemplo, pero RolController solo lista el catálogo para no permitir que se cambien los nombres que controlan el acceso. El CRUD completo de personal es RecepcionistaController.
- Se conservan Bootstrap, estilos.css, el login, el panel, las tarjetas de perfil y las pestañas de reportes. Se completan o adaptan las pantallas de socios, membresías, planes y pagos y se agregan los formularios y el listado de recepcionistas.
- En membresías se usan ID_PLAN y PRECIO_COBRADO, no TIPO ni PRECIO. El plan determina el precio y la duración; los cobros históricos no cambian al editar el precio del catálogo. La gráfica usa las membresías iniciadas por mes, no asistencia, pues no existe una tabla de asistencias.
- Los reportes muestran datos de MySQL, con filtro de fecha de pagos y periodo de membresías; el listado de socios y los indicadores de activos/vigentes son actuales, porque SOCIO no contiene fecha de registro. Se guarda, consulta, edita y elimina el historial de parámetros del reporte (no una copia congelada de los resultados). El perfil permite actualizar datos de contacto; se retiró el botón de carga de foto porque no existía servlet ni columna para fotos.
- Las eliminaciones que estén bloqueadas por claves foráneas muestran un mensaje. Por ejemplo, un recepcionista con socios, pagos o reportes asociados no se elimina para no perder historial.

## Cómo ejecutarlo en IntelliJ
1. Descomprime el ZIP y abre la carpeta Gym-Track (la que tiene pom.xml) como proyecto Maven. Selecciona JDK 17 o compatible con la versión de Spring Boot del pom.
2. En MySQL Workbench, para una base nueva de práctica ejecuta sql/base.sql. **ATENCIÓN: contiene DROP DATABASE y borra todos los registros anteriores de GYMTRACK.** Si tu base ya tiene las siete tablas del SQL original, ejecuta únicamente sql/agregar_admin.sql para añadir la cuenta de prueba sin borrar datos.
3. Configura tu usuario y contraseña locales de MySQL en src/main/resources/application.yaml. Por defecto usa root y contraseña vacía; puedes sustituir `${DB_PASSWORD:}` por tu contraseña local o configurar las variables de entorno DB_USER y DB_PASSWORD en la ejecución de IntelliJ. No subas la contraseña real a Git.
4. Recarga Maven y ejecuta DemoApplication.java. También puedes ejecutar desde la terminal `./mvnw spring-boot:run` (Linux/macOS) o `mvnw.cmd spring-boot:run` (Windows). Se conserva el empaquetado WAR y el Tomcat embebido. Se cambió la distribución del Maven Wrapper a 3.9.9; se mantuvo la versión de Spring Boot del POM original.
5. Abre http://localhost:8081/ . No abras archivos .jsp directamente. Swagger está desactivado en application.yaml: las operaciones se realizan desde las vistas.

## Cuentas de prueba
| Rol | Correo | Contraseña de prueba |
| --- | --- | --- |
| ADMIN | admin@gymtrack.com | AdminGym2026! |
| RECEPCIONISTA | ana.mendoza@ejemplo.com | AnaRecep2026! |

Estas credenciales son exclusivamente para la base local de demostración. El ejemplo escolar y el SQL original guardan contraseñas en texto plano; se conservó ese método de login para respetar lo visto en clase. No publicar este proyecto en Internet ni utilizar credenciales reales. Para producción necesitaría hash seguro de contraseñas y más medidas de seguridad. Se usa un token sencillo en sesión en los formularios POST para reducir solicitudes ajenas.

## Pruebas manuales sugeridas
1. Sin iniciar sesión, visitar /socios o /recepcionistas: debe redirigir a login.
2. Entrar como ADMIN: debe aparecer Recepcionistas. Crear una cuenta, consultar el listado, editarla, cerrar sesión y entrar con esa cuenta. Si no tiene registros relacionados, eliminarla desde la cuenta ADMIN.
3. Entrar como RECEPCIONISTA: no debe aparecer Recepcionistas ni Roles; intentar visitar /recepcionistas y /roles directamente debe redirigir a inicio. Tampoco debe poder guardar o eliminar personal enviando un POST.
4. Registrar un socio y comprobar su rol SOCIO y el responsable. Crear una membresía seleccionando el plan; comprobar fecha y precio. Crear un pago, y confirmar que aparece en Reportes. Probar editar y eliminar registros sin dependencias.
5. Consultar reportes por fechas, generar uno, volver a abrirlo, editar y eliminar del historial. Revisar perfil y cerrar sesión.
6. Comprobar que eliminar un registro referenciado por otro muestra un error en lugar de perder información.

## Estado de verificación
Se revisaron de forma estática las capas, nombres de tablas, rutas y referencias a vistas. No fue posible compilar ni ejecutar MySQL/Tomcat aquí: el Maven Wrapper no pudo descargar Maven y no hay dependencias locales. Por eso las pruebas anteriores están pendientes de ejecutar en tu computadora. No se afirma que el proyecto esté compilado o probado de extremo a extremo.
