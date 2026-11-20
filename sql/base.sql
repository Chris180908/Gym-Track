DROP DATABASE IF EXISTS GYMTRACK;

CREATE DATABASE GYMTRACK
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE GYMTRACK;

-- 1. RECEPCIONISTA
CREATE TABLE RECEPCIONISTA (
                               ID_RECEPCION INT PRIMARY KEY AUTO_INCREMENT,
                               NOM_RECEPCION VARCHAR(100) NOT NULL,
                               CONTRA_RECEPCION VARCHAR(255) NOT NULL,
                               NUM_CALLE_R VARCHAR(20),
                               COL_R VARCHAR(100),
                               EDO_R VARCHAR(100),
                               COD_POST_R VARCHAR(10),
                               TEL_RECEPCION VARCHAR(15),
                               CORREO_RECEPCION VARCHAR(100) UNIQUE
);

-- 2. SOCIO
CREATE TABLE SOCIO (
                       ID_SOCIO INT PRIMARY KEY AUTO_INCREMENT,
                       NOM_SOCIO VARCHAR(100) NOT NULL,
                       CONTRA_SOCIO VARCHAR(255) NOT NULL,
                       NUM_CALLE_S VARCHAR(20),
                       COL_S VARCHAR(100),
                       EDO_S VARCHAR(100),
                       COD_POST_S VARCHAR(10),
                       TEL_SOCIO VARCHAR(15),
                       CORREO_SOCIO VARCHAR(100) UNIQUE,
                       ESTATUS ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
                       ID_RECEPCION INT NOT NULL,
                       FOREIGN KEY (ID_RECEPCION) REFERENCES RECEPCIONISTA(ID_RECEPCION)
                           ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 3. MEMBRESIA
CREATE TABLE MEMBRESIA (
                           ID_MENSUALIDAD INT PRIMARY KEY AUTO_INCREMENT,
                           TIPO VARCHAR(50) NOT NULL DEFAULT 'Mensual', -- Ej: Mensual, Trimestral, Anual
                           PRECIO DECIMAL(10,2) NOT NULL,
                           FECHA_I DATE NOT NULL,
                           FECHA_F DATE NOT NULL,
                           ESTATUS ENUM('VIGENTE', 'VENCIDA', 'CANCELADA') DEFAULT 'VIGENTE',
                           ID_SOCIO INT NOT NULL,
                           FOREIGN KEY (ID_SOCIO) REFERENCES SOCIO(ID_SOCIO)
                               ON DELETE RESTRICT ON UPDATE CASCADE,
                           CONSTRAINT CK_PRECIO CHECK (PRECIO >= 0),
                           CONSTRAINT CK_FECHAS CHECK (FECHA_F >= FECHA_I)
);

-- 4. PAGO
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

-- 5. REPORTE
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

-- 6. USUARIO
CREATE TABLE USUARIO (
                         ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                         USERNAME VARCHAR(100) NOT NULL UNIQUE,
                         PASSWORD VARCHAR(255) NOT NULL,
                         ACTIVO BOOLEAN NOT NULL DEFAULT TRUE
);