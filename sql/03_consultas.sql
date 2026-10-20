-- ============================================================
-- SIGA-RQ - Script 03: consultas
-- ============================================================

SET NAMES utf8mb4;
USE siga_rq;

-- Consulta 1 (RF04): accesos que vencen en los próximos 30 días
SELECT a.apellido, a.nombre, p.nombre AS pack, c.fecha_vencimiento,
       DATEDIFF(c.fecha_vencimiento, CURDATE()) AS dias_restantes
FROM compra c
JOIN alumno a ON a.id_alumno = c.id_alumno
JOIN pack p   ON p.id_pack = c.id_pack
WHERE a.activo = TRUE
  AND c.fecha_vencimiento BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
ORDER BY c.fecha_vencimiento;

-- Consulta 2 (RF06): alumnos que pueden recibir la edición 2
-- (acceso vigente a alguno de los packs de la edición)
SELECT DISTINCT a.apellido, a.nombre, a.email
FROM edicion_pack ep
JOIN compra c ON c.id_pack = ep.id_pack
JOIN alumno a ON a.id_alumno = c.id_alumno
WHERE ep.id_edicion = 2
  AND a.activo = TRUE
  AND c.fecha_vencimiento >= CURDATE();

-- Consulta 3 (RF07): ediciones recibidas por la alumna con id 1
SELECT e.titulo, en.fecha_envio
FROM envio en
JOIN edicion_newsletter e ON e.id_edicion = en.id_edicion
WHERE en.id_alumno = 1
ORDER BY en.fecha_envio DESC;

-- Consulta 4 (RF09): cantidad de ventas e ingresos por pack
SELECT p.nombre AS pack, COUNT(c.id_compra) AS ventas, IFNULL(SUM(c.monto), 0) AS ingresos
FROM pack p
LEFT JOIN compra c ON c.id_pack = p.id_pack
GROUP BY p.id_pack, p.nombre
ORDER BY ingresos DESC;
