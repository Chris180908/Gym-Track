-- SOLO PARA PRUEBAS LOCALES. No ejecutar en producción.
-- Ejecutar una vez sobre la base existente con los roles ya creados.
-- Las dos cuentas usan la contraseña de demostración GymDemo2026!.
USE GYMTRACK;
START TRANSACTION;

INSERT INTO USUARIO (USERNAME, PASSWORD, ACTIVO, ID_ROL)
VALUES ('recepcion@demo.invalid', '$2a$10$VRs1R9HONZwKT8GQW24WpOnkM/hCMfNzYmrDnAUJtm70GRfHokG2W', TRUE,
        (SELECT ID_ROL FROM ROL WHERE NOMBRE = 'RECEPCIONISTA'));
SET @id_recepcion = LAST_INSERT_ID();

INSERT INTO RECEPCIONISTA (ID_RECEPCION, NOM_RECEPCION, CORREO_RECEPCION)
VALUES (@id_recepcion, 'Recepción de prueba', 'recepcion@demo.invalid');

INSERT INTO USUARIO (USERNAME, PASSWORD, ACTIVO, ID_ROL)
VALUES ('socio@demo.invalid', '$2a$10$VRs1R9HONZwKT8GQW24WpOnkM/hCMfNzYmrDnAUJtm70GRfHokG2W', TRUE,
        (SELECT ID_ROL FROM ROL WHERE NOMBRE = 'SOCIO'));
SET @id_socio = LAST_INSERT_ID();

INSERT INTO SOCIO (ID_SOCIO, NOM_SOCIO, CORREO_SOCIO, ESTATUS, ID_RECEPCION)
VALUES (@id_socio, 'Socio de prueba', 'socio@demo.invalid', 'ACTIVO', @id_recepcion);

COMMIT;
SELECT ID, USERNAME FROM USUARIO WHERE USERNAME IN ('recepcion@demo.invalid', 'socio@demo.invalid');
