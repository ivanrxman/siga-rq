-- ============================================================
-- SIGA-RQ - Script 04: borrado de registros
-- ============================================================

SET NAMES utf8mb4;
USE siga_rq;

-- 1) Este borrado da ERROR 1451 porque el pack tiene compras asociadas
--    (la clave foránea protege la integridad de los datos).
--    Se deja comentado para que el script se ejecute completo.
-- DELETE FROM pack WHERE id_pack = 1;

-- 2) Borrar un envío puntual
DELETE FROM envio WHERE id_edicion = 2 AND id_alumno = 4;

-- 3) Borrar la edición 2: por ON DELETE CASCADE también se borran
--    sus filas en edicion_pack y envio
DELETE FROM edicion_newsletter WHERE id_edicion = 2;

-- 4) Borrar todos los datos de prueba (primero las tablas hijas)
DELETE FROM envio;
DELETE FROM edicion_pack;
DELETE FROM edicion_newsletter;
DELETE FROM avance;
DELETE FROM compra;
DELETE FROM modulo;
DELETE FROM alumno;
DELETE FROM pack;
DELETE FROM usuario;

-- 5) Verificación: todas las tablas deben quedar en 0
SELECT 'alumno' AS tabla, COUNT(*) AS filas FROM alumno
UNION ALL SELECT 'pack', COUNT(*) FROM pack
UNION ALL SELECT 'compra', COUNT(*) FROM compra
UNION ALL SELECT 'edicion_newsletter', COUNT(*) FROM edicion_newsletter
UNION ALL SELECT 'envio', COUNT(*) FROM envio;
