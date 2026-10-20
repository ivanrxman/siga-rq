-- ============================================================
-- SIGA-RQ - Script 01: creación de la base de datos y tablas
-- Seminario de Práctica de Informática - Ivan Roman
-- ============================================================

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS siga_rq;
CREATE DATABASE siga_rq CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE siga_rq;

-- Usuarios que pueden ingresar al sistema (la clave se guarda cifrada con SHA2)
CREATE TABLE usuario (
    id_usuario   INT AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(100) NOT NULL UNIQUE,
    clave_hash   CHAR(64)     NOT NULL,
    rol          VARCHAR(10)  NOT NULL      -- 'ADMIN' o 'ALUMNO'
);

-- Alumnos de la academia (activo = FALSE significa dado de baja)
CREATE TABLE alumno (
    id_alumno    INT AUTO_INCREMENT PRIMARY KEY,
    dni          VARCHAR(10)  NOT NULL UNIQUE,
    nombre       VARCHAR(50)  NOT NULL,
    apellido     VARCHAR(50)  NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    telefono     VARCHAR(20),
    fecha_alta   DATE         NOT NULL,
    activo       BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Packs de contenido on-demand
CREATE TABLE pack (
    id_pack           INT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(40)   NOT NULL UNIQUE,
    precio            DECIMAL(10,2) NOT NULL,
    nivel_newsletter  INT           NOT NULL   -- 1 = básico, 2 = medio, 3 = completo
);

-- Módulos (clases grabadas) que forman cada pack
CREATE TABLE modulo (
    id_modulo  INT AUTO_INCREMENT PRIMARY KEY,
    id_pack    INT          NOT NULL,
    titulo     VARCHAR(100) NOT NULL,
    orden      INT          NOT NULL,
    FOREIGN KEY (id_pack) REFERENCES pack (id_pack)
);

-- Compras: pago único con 365 días de acceso
CREATE TABLE compra (
    id_compra          INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno          INT           NOT NULL,
    id_pack            INT           NOT NULL,
    fecha_pago         DATE          NOT NULL,
    monto              DECIMAL(10,2) NOT NULL,
    fecha_vencimiento  DATE          NOT NULL,   -- fecha de inicio + 365 días
    FOREIGN KEY (id_alumno) REFERENCES alumno (id_alumno),
    FOREIGN KEY (id_pack)   REFERENCES pack (id_pack)
);

-- Ediciones de la newsletter
CREATE TABLE edicion_newsletter (
    id_edicion         INT AUTO_INCREMENT PRIMARY KEY,
    titulo             VARCHAR(150) NOT NULL,
    resumen            TEXT         NOT NULL,
    fecha_publicacion  DATE         NOT NULL
);

-- Packs a los que va dirigida cada edición (relación muchos a muchos)
CREATE TABLE edicion_pack (
    id_edicion  INT NOT NULL,
    id_pack     INT NOT NULL,
    PRIMARY KEY (id_edicion, id_pack),
    FOREIGN KEY (id_edicion) REFERENCES edicion_newsletter (id_edicion) ON DELETE CASCADE,
    FOREIGN KEY (id_pack)    REFERENCES pack (id_pack)
);

-- Envíos: qué edición recibió cada alumno y cuándo (trazabilidad)
CREATE TABLE envio (
    id_envio     INT AUTO_INCREMENT PRIMARY KEY,
    id_edicion   INT      NOT NULL,
    id_alumno    INT      NOT NULL,
    fecha_envio  DATETIME NOT NULL,
    FOREIGN KEY (id_edicion) REFERENCES edicion_newsletter (id_edicion) ON DELETE CASCADE,
    FOREIGN KEY (id_alumno)  REFERENCES alumno (id_alumno)
);

-- Avance: módulos que completó cada alumno
CREATE TABLE avance (
    id_alumno         INT  NOT NULL,
    id_modulo         INT  NOT NULL,
    fecha_completado  DATE NOT NULL,
    PRIMARY KEY (id_alumno, id_modulo),
    FOREIGN KEY (id_alumno) REFERENCES alumno (id_alumno),
    FOREIGN KEY (id_modulo) REFERENCES modulo (id_modulo)
);
