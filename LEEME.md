# GymTrack Android – Java

App de socios, minSdk 24 (Android 7.0). Se conserva el esqueleto com.gymtrack.app, Gradle wrapper y compileSdk 37 del proyecto recibido; se ajusta targetSdk a 35 para la versión de prueba local, sin cambiar minSdk. Interfaz nativa en Java, sin dependencias de UI externas, con fondo oscuro, tarjetas y navegación inferior basada en tus capturas. El código QR se deja pendiente.

## Ejecutar
1. Instala el parche de backend siguiendo su LEEME.md. Ejecuta MySQL y Spring Boot (puerto 8081).
2. Descomprime este ZIP. En Android Studio, File > Open, selecciona GymTrack (la carpeta con settings.gradle.kts). No crees otro proyecto.
3. Permite que Gradle sincronice. Usa el JDK / SDK requerido por la configuración original (AGP 9.3.3, Gradle 9.5.0, compileSdk 37 y daemon JDK 25). Android Studio puede pedir que descargues el SDK de compilación y que localices tu SDK. `local.properties` de otra computadora se excluyó intencionalmente.
4. Selecciona un emulador oficial con Android 7.0 (API 24) o superior, y la variante debug. Pulsa Run. La app usa por defecto http://10.0.2.2:8081/api/movil/ (dirección del host desde el emulador).
5. Para probar en teléfono físico, conecta el equipo y el teléfono a la misma red privada. En `app/src/main/java/com/gymtrack/app/Api.java` cambia BASE_URL a `http://IP_DE_TU_PC:8081/api/movil/` (por ejemplo, una IP local 192.168.x.x; no uses localhost). Asegúrate de que el servidor permita conexiones desde esa red y de autorizar el puerto solo en tu red privada. Si desplegaste un WAR con prefijo, agrega ese prefijo antes de /api/movil/. No expongas el puerto a Internet.
6. Usa correo y contraseña de un socio registrado en la base. Si usaste los datos de prueba de tu SQL original, consulta el correo y contraseña correspondientes. No hay registro desde la app.

## Archivos importantes
- `MainActivity.java`: interfaz, perfil, membresía, planes, edición, sesión y gestión de errores.
- `Api.java`: HTTP/JSON con HttpURLConnection, sin conexiones en el hilo principal, compatible con API 24.
- `res/layout/activity_main.xml`: contenedor raíz; vistas se generan con Java.
- `res/values/themes.xml`, `res/drawable/`: tema y recursos visuales.
- `AndroidManifest.xml`: Internet, sin backup de tokens; tráfico HTTP habilitado solo en debug mediante su manifiesto.

## Uso
- Perfil: ID, nombre, correo, teléfono, estado del socio, primera membresía, editar nombre/correo/teléfono, cerrar sesión.
- Membresía: plan, estado, fechas, precio cobrado, días restantes si está vigente.
- Planes: catálogo activo con precios y duraciones. No hay pagos ni contratación móvil; todo eso se realiza en recepción.
- El icono de actualización consulta nuevamente el backend. Marcar mantener iniciada guarda únicamente el token de sesión, no la contraseña. Si reinicias el backend, tendrás que volver a entrar. Si no marcas recordar, el token solo se conserva mientras la actividad vive (incluyendo rotaciones). Para cerrar una sesión, usa el botón correspondiente.

## Solución de problemas
- Error de conexión: revisa backend, puerto 8081, BASE_URL y variante debug. `localhost` dentro del emulador no es la computadora.
- Error 404: comprueba que aplicaste el parche y el prefijo de despliegue.
- Error de correo/contraseña: utiliza una cuenta de SOCIO, no de administrador/recepción.
- Error de SDK/Gradle: instala las versiones del esqueleto original o adapta las versiones a tu Android Studio antes de compilar.

## Alcance de verificación y seguridad
Se revisó la sintaxis de los Java modificados y que los XML estén bien formados. No se generó APK ni se probó el flujo en emulador / MySQL porque el entorno de preparación no incluye Android SDK ni Maven. HTTP local es solo para desarrollo con datos de prueba; para producción cambia BASE_URL a HTTPS y aplica las recomendaciones del parche backend. El target 35 no es una promesa de cumplimiento de requisitos de Google Play; es una configuración para probar localmente.
