DROP DATABASE IF EXISTS GYMTRACK;

CREATE DATABASE GYMTRACK
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE GYMTRACK;


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

                       ID_RECEPCION INT NOT NULL,

                       FOREIGN KEY (ID_RECEPCION)
                           REFERENCES RECEPCIONISTA(ID_RECEPCION)
                           ON DELETE RESTRICT
                           ON UPDATE CASCADE
);


CREATE TABLE MEMBRESIA (
                           ID_MENSUALIDAD INT PRIMARY KEY AUTO_INCREMENT,
                           PRECIO DECIMAL(10,2) NOT NULL,
                           FECHA_I DATE NOT NULL,
                           FECHA_F DATE NOT NULL,

                           ID_SOCIO INT NOT NULL,

                           FOREIGN KEY (ID_SOCIO)
                               REFERENCES SOCIO(ID_SOCIO)
                               ON DELETE RESTRICT
                               ON UPDATE CASCADE,

                           CONSTRAINT CK_PRECIO
                               CHECK (PRECIO >= 0),

                           CONSTRAINT CK_FECHAS
                               CHECK (FECHA_F >= FECHA_I)
);

CREATE TABLE REPORTE (
                         ID_REPORTE INT PRIMARY KEY AUTO_INCREMENT,
                         FECHA_G DATE NOT NULL,

                         ID_RECEPCION INT NOT NULL,

                         FOREIGN KEY (ID_RECEPCION)
                             REFERENCES RECEPCIONISTA(ID_RECEPCION)
                             ON DELETE RESTRICT
                             ON UPDATE CASCADE
);