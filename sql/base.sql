-- SOLO para una instalación limpia de prueba: el siguiente DROP elimina todos los datos de GYMTRACK.
-- Si ya tienes datos, NO ejecutes este archivo; revisa agregar_admin.sql.
DROP DATABASE IF EXISTS GYMTRACK;

CREATE DATABASE GYMTRACK
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE GYMTRACK;

-- ==========================================
-- 0. ROL
-- ==========================================
CREATE TABLE ROL (
                     ID_ROL INT PRIMARY KEY AUTO_INCREMENT,
                     NOMBRE VARCHAR(100) NOT NULL UNIQUE,
                     DESCRIPCION VARCHAR(100)
);

-- ==========================================
-- 1. RECEPCIONISTA (Conectada a ROL)
-- ==========================================
CREATE TABLE RECEPCIONISTA (
                               ID_RECEPCION INT PRIMARY KEY AUTO_INCREMENT,
                               NOM_RECEPCION VARCHAR(100) NOT NULL,
                               CONTRA_RECEPCION VARCHAR(255) NOT NULL,
                               NUM_CALLE_R VARCHAR(100),
                               COL_R VARCHAR(100),
                               EDO_R VARCHAR(100),
                               COD_POST_R VARCHAR(10),
                               TEL_RECEPCION VARCHAR(15),
                               CORREO_RECEPCION VARCHAR(100) UNIQUE,
                               ID_ROL INT NOT NULL DEFAULT 2,
                               FOREIGN KEY (ID_ROL) REFERENCES ROL(ID_ROL)
                                   ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ==========================================
-- 2. SOCIO (Conectada a RECEPCIONISTA y ROL)
-- ==========================================
CREATE TABLE SOCIO (
                       ID_SOCIO INT PRIMARY KEY AUTO_INCREMENT,
                       NOM_SOCIO VARCHAR(100) NOT NULL,
                       CONTRA_SOCIO VARCHAR(255) NOT NULL,
                       NUM_CALLE_S VARCHAR(100),
                       COL_S VARCHAR(100),
                       EDO_S VARCHAR(100),
                       COD_POST_S VARCHAR(10),
                       TEL_SOCIO VARCHAR(15),
                       CORREO_SOCIO VARCHAR(100) UNIQUE,
                       ESTATUS ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
                       ID_RECEPCION INT NOT NULL,
                       ID_ROL INT NOT NULL DEFAULT 3,
                       FOREIGN KEY (ID_RECEPCION) REFERENCES RECEPCIONISTA(ID_RECEPCION)
                           ON DELETE RESTRICT ON UPDATE CASCADE,
                       FOREIGN KEY (ID_ROL) REFERENCES ROL(ID_ROL)
                           ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ==========================================
-- 3. PLAN_MEMBRESIA (Catálogo de planes y tarifas)
-- ==========================================
CREATE TABLE PLAN_MEMBRESIA (
                                ID_PLAN INT PRIMARY KEY AUTO_INCREMENT,
                                NOMBRE VARCHAR(50) NOT NULL UNIQUE,
                                DURACION_DIAS INT NOT NULL,
                                PRECIO DECIMAL(10,2) NOT NULL,
                                ACTIVO BOOLEAN NOT NULL DEFAULT TRUE,
                                CONSTRAINT CK_PLAN_PRECIO CHECK (PRECIO >= 0),
                                CONSTRAINT CK_PLAN_DIAS CHECK (DURACION_DIAS > 0)
);

-- ==========================================
-- 4. MEMBRESIA (Conectada a SOCIO y PLAN_MEMBRESIA)
-- ==========================================
CREATE TABLE MEMBRESIA (
                           ID_MENSUALIDAD INT PRIMARY KEY AUTO_INCREMENT,
                           PRECIO_COBRADO DECIMAL(10,2) NOT NULL,
                           FECHA_I DATE NOT NULL,
                           FECHA_F DATE NOT NULL,
                           ESTATUS ENUM('VIGENTE', 'VENCIDA', 'CANCELADA') DEFAULT 'VIGENTE',
                           ID_SOCIO INT NOT NULL,
                           ID_PLAN INT NOT NULL,
                           FOREIGN KEY (ID_SOCIO) REFERENCES SOCIO(ID_SOCIO)
                               ON DELETE RESTRICT ON UPDATE CASCADE,
                           FOREIGN KEY (ID_PLAN) REFERENCES PLAN_MEMBRESIA(ID_PLAN)
                               ON DELETE RESTRICT ON UPDATE CASCADE,
                           CONSTRAINT CK_PRECIO CHECK (PRECIO_COBRADO >= 0),
                           CONSTRAINT CK_FECHAS CHECK (FECHA_F >= FECHA_I)
);

-- ==========================================
-- 5. PAGO (Conectada a MEMBRESIA y RECEPCIONISTA)
-- ==========================================
CREATE TABLE PAGO (
                      ID_PAGO INT PRIMARY KEY AUTO_INCREMENT,
                      MONTO DECIMAL(10,2) NOT NULL,
                      FECHA_PAGO DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      METODO_PAGO ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA') NOT NULL,
                      CONCEPTO VARCHAR(100) NOT NULL,
                      ID_MENSUALIDAD INT NOT NULL,
                      ID_RECEPCION INT NOT NULL,
                      FOREIGN KEY (ID_MENSUALIDAD) REFERENCES MEMBRESIA(ID_MENSUALIDAD)
                          ON DELETE RESTRICT ON UPDATE CASCADE,
                      FOREIGN KEY (ID_RECEPCION) REFERENCES RECEPCIONISTA(ID_RECEPCION)
                          ON DELETE RESTRICT ON UPDATE CASCADE,
                      CONSTRAINT CK_MONTO_PAGO CHECK (MONTO > 0)
);

-- ==========================================
-- 6. REPORTE (Conectada a RECEPCIONISTA)
-- ==========================================
CREATE TABLE REPORTE (
                         ID_REPORTE INT PRIMARY KEY AUTO_INCREMENT,
                         TIPO_REPORTE ENUM('GENERAL', 'SOCIOS', 'MEMBRESIAS', 'PAGOS') NOT NULL,
                         FECHA_INICIO_FILTRO DATE,
                         FECHA_FIN_FILTRO DATE,
                         FECHA_G DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         ID_RECEPCION INT NOT NULL,
                         FOREIGN KEY (ID_RECEPCION) REFERENCES RECEPCIONISTA(ID_RECEPCION)
                             ON DELETE RESTRICT ON UPDATE CASCADE
);

-- =========================================================================
-- INSERTS DE DATOS DE PRUEBA
-- =========================================================================

-- 0. ROL
INSERT INTO ROL (NOMBRE, DESCRIPCION) VALUES
                                          ('ADMIN', 'Administrador general con acceso total al sistema'),
                                          ('RECEPCIONISTA', 'Personal de mostrador encargado de registros y cobros'),
                                          ('SOCIO', 'Cliente miembro del gimnasio con acceso a consulta de membresía');

-- 1. RECEPCIONISTA
INSERT INTO RECEPCIONISTA (
    NOM_RECEPCION, CONTRA_RECEPCION, NUM_CALLE_R, COL_R, EDO_R, COD_POST_R, TEL_RECEPCION, CORREO_RECEPCION, ID_ROL
) VALUES
      ('Ana Sofía Mendoza', 'AnaRecep2026!', 'Av. Insurgentes Sur 1450', 'Actipan', 'Ciudad de México', '03230', '5512345678', 'ana.mendoza@ejemplo.com', 2),
      ('Carlos Eduardo Ríos', 'Carlos12345', 'Calle Reforma 320', 'Centro', 'Jalisco', '44100', '3398765432', 'carlos.rios@ejemplo.com', 2),
      ('Mariana López Castro', 'Mariana_Pass99', 'Blvd. Díaz Ordaz 890', 'Santa María', 'Nuevo León', '64650', '8145678901', 'mariana.lopez@ejemplo.com', 2);

-- Cuenta de administrador de prueba. Se agrega al final para no cambiar IDs de los datos anteriores.
INSERT INTO RECEPCIONISTA (NOM_RECEPCION, CONTRA_RECEPCION, CORREO_RECEPCION, ID_ROL)
VALUES ('Administrador GymTrack', 'AdminGym2026!', 'admin@gymtrack.com', 1);

-- 2. SOCIO
INSERT INTO SOCIO (
    NOM_SOCIO, CONTRA_SOCIO, NUM_CALLE_S, COL_S, EDO_S, COD_POST_S, TEL_SOCIO, CORREO_SOCIO, ESTATUS, ID_RECEPCION, ID_ROL
) VALUES
      ('Roberto Gómez Hernández', 'RobGym2026$', 'Calle 10 #45', 'San Pedro de los Pinos', 'Ciudad de México', '03800', '5523456789', 'roberto.gomez@correo.com', 'ACTIVO', 1, 3),
      ('Valeria Torres Silva', 'ValeFit_88', 'Av. Vallarta 2100', 'Arcos Vallarta', 'Jalisco', '44130', '3311223344', 'valeria.torres@correo.com', 'ACTIVO', 2, 3),
      ('Fernando Ruiz Díaz', 'FerRuiz#12', 'Calzada del Valle 402', 'Del Valle', 'Nuevo León', '66220', '8199887766', 'fernando.ruiz@correo.com', 'INACTIVO', 3, 3),
      ('Lucía Ramírez Ortega', 'LuciRamirez95', 'Eje 5 Sur 112', 'Narvarte', 'Ciudad de México', '03020', '5587654321', 'lucia.ramirez@correo.com', 'ACTIVO', 1, 3),
      ('Diego Morales Cruz', 'DiegoM_2026', 'Av. Patria 1500', 'Jardines Universidad', 'Jalisco', '45110', '3344556677', 'diego.morales@correo.com', 'ACTIVO', 2, 3);

-- 3. PLAN_MEMBRESIA
INSERT INTO PLAN_MEMBRESIA (NOMBRE, DURACION_DIAS, PRECIO, ACTIVO) VALUES
                                                                       ('Mensual', 30, 300.00, TRUE),
                                                                       ('Trimestral', 90, 850.00, TRUE),
                                                                       ('Anual', 365, 2900.00, TRUE);

-- 4. MEMBRESIA (ID_PLAN: 1 = Mensual, 2 = Trimestral, 3 = Anual)
INSERT INTO MEMBRESIA (PRECIO_COBRADO, FECHA_I, FECHA_F, ESTATUS, ID_SOCIO, ID_PLAN) VALUES
                                                                                         (300.00, '2026-11-01', '2026-12-01', 'VIGENTE', 1, 1),
                                                                                         (850.00, '2026-10-15', '2027-01-13', 'VIGENTE', 2, 2),
                                                                                         (300.00, '2026-08-01', '2026-08-31', 'VENCIDA', 3, 1),
                                                                                         (2900.00, '2026-06-01', '2027-06-01', 'VIGENTE', 4, 3),
                                                                                         (300.00, '2026-11-10', '2026-12-10', 'VIGENTE', 5, 1);

-- 5. PAGO (Montos alineados con los planes contratados)
INSERT INTO PAGO (MONTO, FECHA_PAGO, METODO_PAGO, CONCEPTO, ID_MENSUALIDAD, ID_RECEPCION) VALUES
                                                                                              (300.00, '2026-11-01 09:30:00', 'TARJETA', 'Pago plan Mensual Noviembre - Roberto Gómez', 1, 1),
                                                                                              (850.00, '2026-10-15 16:45:10', 'TRANSFERENCIA', 'Pago plan Trimestral - Valeria Torres', 2, 2),
                                                                                              (300.00, '2026-08-01 11:15:00', 'EFECTIVO', 'Pago plan Mensual Agosto - Fernando Ruiz', 3, 3),
                                                                                              (2900.00, '2026-06-01 18:20:30', 'TARJETA', 'Pago plan Anual - Lucía Ramírez', 4, 1),
                                                                                              (300.00, '2026-11-10 08:05:45', 'EFECTIVO', 'Pago plan Mensual Noviembre - Diego Morales', 5, 2);

-- 6. REPORTE
INSERT INTO REPORTE (TIPO_REPORTE, FECHA_INICIO_FILTRO, FECHA_FIN_FILTRO, FECHA_G, ID_RECEPCION) VALUES
                                                                                                     ('PAGOS', '2026-10-01', '2026-10-31', '2026-11-01 20:00:00', 1),
                                                                                                     ('SOCIOS', '2026-01-01', '2026-11-15', '2026-11-16 10:15:00', 2),
                                                                                                     ('MEMBRESIAS', '2026-08-01', '2026-11-20', '2026-11-21 14:30:00', 3),
                                                                                                     ('GENERAL', '2026-06-01', '2026-11-25', CURRENT_TIMESTAMP, 1);