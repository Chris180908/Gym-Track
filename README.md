# GymTrack — backend escolar, versión CRUD

Proyecto Java 17, Spring Boot, Maven, Lombok, JPA, JSP y MySQL. Basado en el
proyecto Escuela-SpringBoot y en la versión anterior de GymTrack. Se conservan
los nombres `GymTrack.example.demo.Repository` y `GymTrack.example.demo.Service`.
No se usa Swagger ni springdoc. La base SQL se conserva sin cambios (10 tablas,
identificadores BIGINT y sin CHECK).

## 1. Ejecutar

1. Descomprime este ZIP en una carpeta nueva y abre el proyecto Maven en IntelliJ
   IDEA con JDK 17. Conserva tu versión anterior como respaldo.
2. Si ya creaste GYMTRACK, **no ejecutes sql/base.sql**: elimina todos los datos.
3. Para una base exclusivamente local de pruebas, si no existen las cuentas de
   ejemplo, ejecuta `sql/datos_prueba.sql` una sola vez.
4. En Run > Edit Configurations > Environment variables, configura DB_USERNAME y
   DB_PASSWORD con tus credenciales locales. El usuario por defecto es root.
5. Recarga Maven y ejecuta DemoApplication. Alternativamente, desde PowerShell:

   ```powershell
   $env:DB_USERNAME = 'root'
   $env:DB_PASSWORD = 'TU_CONTRASEÑA_LOCAL'
   .\mvnw.cmd spring-boot:run
   ```

   Si el wrapper falla, utiliza el Maven integrado de IntelliJ. No se pudo
   descargar la distribución del wrapper durante la revisión de esta versión.
6. Abre http://localhost:8081/ para la web. Los endpoints REST se prueban con
   `pruebas-api.http` (IntelliJ HTTP Client, si está disponible) o Postman.

Se conserva `ddl-auto: validate`, el puerto 8081 y la dirección local 127.0.0.1.
El emulador de Android utilizará 10.0.2.2:8081. No exponer esta versión escolar a
internet ni usar datos reales. No cambiar a `ddl-auto: create` para ocultar errores.

## 2. Cuentas ficticias (solo desarrollo local)

| Rol | Usuario | Contraseña |
|---|---|---|
| RECEPCIONISTA | recepcion@demo.invalid | GymDemo2026! |
| SOCIO | socio@demo.invalid | GymDemo2026! |

El SQL guarda hashes BCrypt; no se incluye la contraseña de tu MySQL. El login
REST es `POST /api/access`. Envía username y password como JSON y conserva la
cookie JSESSIONID para las siguientes peticiones. El login web se conserva.

## 3. CRUD de recepción

Todos los recursos de esta tabla tienen GET de lista y GET por `/{id}`. Para POST
se usa la ruta base y para PUT/DELETE se agrega `/{id}`. Todos requieren sesión
con rol RECEPCIONISTA. El socio solo usa sus endpoints móviles.

| Recurso | Ruta | POST | PUT | DELETE |
|---|---|---|---|---|
| Rol | /api/roles | Alta | Nombre y descripción | Física, salvo roles del sistema y roles referenciados |
| Usuario | /api/usuarios | Cuenta con perfil | Username, contraseña y activo | Lógica |
| Recepcionista | /api/recepcionistas | Cuenta y perfil | Datos personales | Lógica en Usuario |
| Socio | /api/socios | Cuenta y perfil | Datos personales | Lógica en Usuario y Socio |
| Plan | /api/planes | Alta | Datos del plan | Lógica |
| Suscripción | /api/suscripciones | Pendiente local | Plan y estado local | Cancelación local |
| Membresía | /api/membresias | Prepago presencial y pago | Fechas y estado del prepago | Cancelación local |
| Pago | /api/pagos | Pago presencial de prueba para membresía sin pago | Concepto de pago presencial | No disponible |
| Reporte | /api/reportes | Alta | Tipo y fechas | Física |
| Acceso | /api/accesos | Validación manual y registro | No disponible | No disponible |

Reglas adicionales:

- Los roles SOCIO y RECEPCIONISTA no se renombran ni eliminan. Otros roles se
  pueden registrar como catálogo, pero no pueden iniciar sesión hasta que se
  implemente su perfil y permisos.
- POST de Usuario crea también su perfil en una transacción. Socio y
  Recepcionista reutilizan el ID de Usuario. No se permiten cambios de rol desde
  PUT para evitar perfiles inconsistentes. La contraseña se cambia con BCrypt.
  No se devuelve la contraseña y no se permite desactivar la propia cuenta.
  Reactivar una cuenta de socio también reactiva su perfil.
- POST de membresía calcula precio y fechas a partir del plan, registra el pago
  de prueba en la misma transacción y añade periodos sin solapamiento. PUT permite
  corregir fechas y estado de un prepago, sin cambiar socio, precio ni plan. DELETE
  no elimina el pago ni genera reembolso. FECHA_F se considera inclusiva.
- POST de Pago es solo para registros de prepago previos que no tengan pago. Una
  venta creada con POST de Membresía ya tiene pago, por lo que volver a registrarlo
  se rechaza con 409. PUT de Pago solo corrige el concepto; no altera el importe,
  método, fecha, socio ni referencia. No hay cobros reales ni reembolsos.
- Suscripción local puede quedar PENDIENTE, SUSPENDIDA o CANCELADA; ACTIVA está
  bloqueada mientras no exista una pasarela. No se editan referencias externas ni
  se cancela un contrato real mediante este CRUD. No hay renovación automática.
- POST de Acceso verifica cuenta activa, perfil activo, periodo vigente y existencia
  de pago. El servidor decide PERMITIDO o DENEGADO; no recibe ese resultado del
  cliente. Se conserva el historial sin PUT/DELETE. No es un lector QR.
- HTTP 200: consulta/cambio; 201: alta; 204: baja; 400: validación; 401: sin sesión;
  403: rol no autorizado; 404: ID inexistente; 409: conflicto de datos o de negocio.
  Son resultados previstos, aún no comprobados contra un servidor en ejecución.

### JSON de altas y cambios

POST `/api/socios` y `/api/recepcionistas` comparten los campos personales del DTO
RegistroSocio (nombre histórico del proyecto):

```json
{
    "username": "nuevo@demo.invalid",
    "password": "OtroDemo2026!",
    "nombre": "Perfil de prueba",
    "correo": "nuevo@demo.invalid",
    "telefono": "5500000000",
    "numCalle": "Calle de prueba 10",
    "colonia": "Centro",
    "estado": "CDMX",
    "codigoPostal": "06000"
}
```

POST `/api/usuarios` recibe `{"rol":"SOCIO","datos":{...}}`, con los campos
anteriores dentro de datos. Puede indicar RECEPCIONISTA como rol. Para PUT de
Usuario se envía `{"username":"otro@demo.invalid","activo":true}` y opcionalmente
password para sustituirla. Si un campo no se manda, se conserva. Para PUT de Socio
 o Recepcionista usa los datos personales completos sin username/password.

Consulta `pruebas-api.http` para ejemplos adicionales de cada recurso. Cambia los
IDs por los que existan en tu base de datos; usa registros nuevos de prueba antes
de probar modificaciones o bajas.

## 4. Android (contrato de API sin cambios)

| Método | Ruta | Función |
|---|---|---|
| POST | /api/access | Login |
| GET | /api/movil/perfil | Perfil propio |
| PUT | /api/movil/perfil | Actualizar datos personales |
| GET | /api/movil/membresias | Membresías y estado según fechas |
| GET | /api/movil/pagos | Historial propio |
| GET | /api/movil/planes | Planes activos |
| POST | /api/access/logout | Cerrar sesión |

Android conserva JSESSIONID mediante Retrofit/OkHttp y CookieJar. Los endpoints
móviles obtienen el ID de la sesión, nunca de un ID de socio enviado por el cliente.

## 5. Alcance y pruebas pendientes

Se conserva la estructura Model, Repository, Service, Service/impl, Controller y
DTO; se añaden métodos explícitos como en el ejemplo de Escuela. Las JSP mantienen
su diseño y consultas existentes; faltan los formularios para operaciones CRUD.
No se modificó Android. La base de datos no se alteró. Se quitó springdoc del YAML,
la dependencia del pom, la clase OpenApiConfig y las anotaciones de documentación.

Esta versión sigue siendo escolar. Todos los recepcionistas tienen los mismos
permisos porque solo existen dos roles operativos. Antes de usarla en un gimnasio
real hacen falta permisos administrativos diferenciados, HTTPS, CSRF, protección
de intentos de login, revisión de concurrencia y pruebas de integración. PayPal,
Google Pay, webhooks, reembolsos, renovación automática y QR quedan pendientes.

Se intentó compilar con Maven, pero falló la descarga de la distribución. Por ello
las pruebas contra MySQL y la compilación completa quedan pendientes en IntelliJ.

Comprobaciones estáticas de esta entrega: 85 archivos Java analizados con el
parser de Java 17, sin errores sintácticos; POM y YAML válidos como documentos;
SQL idéntico al de la entrega anterior; rutas CRUD comprobadas en el código.
Estos resultados no sustituyen la compilación ni las pruebas de ejecución.
