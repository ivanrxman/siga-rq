-- ============================================================
-- SIGA-RQ - Script 02: inserción de datos de prueba
-- Las fechas se calculan a partir de la fecha actual (CURDATE())
-- para que siempre haya accesos vigentes, por vencer y vencidos.
-- ============================================================

SET NAMES utf8mb4;
USE siga_rq;

INSERT INTO usuario (email, clave_hash, rol) VALUES
('admin@rquants.com', SHA2('admin123', 256), 'ADMIN');

INSERT INTO pack (nombre, precio, nivel_newsletter) VALUES
('Fundamentos', 90000.00, 1),
('Avanzado',   180000.00, 2),
('Premium',    320000.00, 3);

INSERT INTO modulo (id_pack, titulo, orden) VALUES
(1, 'Introducción a los mercados', 1),
(1, 'Renta fija y renta variable', 2),
(2, 'Python para finanzas', 1),
(2, 'Análisis cuantitativo', 2),
(3, 'Modelos de IA aplicados a inversiones', 1),
(3, 'Backtesting de estrategias', 2);

INSERT INTO alumno (dni, nombre, apellido, email, telefono, fecha_alta) VALUES
('38111222', 'Lucía',     'Fernández', 'lucia.fernandez@mail.com', '3515551001', DATE_SUB(CURDATE(), INTERVAL 340 DAY)),
('40222333', 'Martín',    'Gómez',     'martin.gomez@mail.com',    '3515551002', DATE_SUB(CURDATE(), INTERVAL 200 DAY)),
('35333444', 'Sofía',     'Ramírez',   'sofia.ramirez@mail.com',   '1155551003', DATE_SUB(CURDATE(), INTERVAL 400 DAY)),
('42444555', 'Tomás',     'Herrera',   'tomas.herrera@mail.com',   '2615551004', DATE_SUB(CURDATE(), INTERVAL 355 DAY)),
('37555666', 'Valentina', 'López',     'valentina.lopez@mail.com', '3415551005', DATE_SUB(CURDATE(), INTERVAL 30 DAY));

-- La fecha de vencimiento se calcula como fecha de pago + 365 días
INSERT INTO compra (id_alumno, id_pack, fecha_pago, monto, fecha_vencimiento) VALUES
(1, 2, DATE_SUB(CURDATE(), INTERVAL 340 DAY), 180000.00, DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 340 DAY), INTERVAL 365 DAY)),
(2, 3, DATE_SUB(CURDATE(), INTERVAL 200 DAY), 320000.00, DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 200 DAY), INTERVAL 365 DAY)),
(3, 1, DATE_SUB(CURDATE(), INTERVAL 400 DAY),  90000.00, DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 400 DAY), INTERVAL 365 DAY)),
(4, 2, DATE_SUB(CURDATE(), INTERVAL 355 DAY), 180000.00, DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 355 DAY), INTERVAL 365 DAY)),
(5, 1, DATE_SUB(CURDATE(), INTERVAL 30 DAY),   90000.00, DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 30 DAY),  INTERVAL 365 DAY));

INSERT INTO avance (id_alumno, id_modulo, fecha_completado) VALUES
(1, 3, DATE_SUB(CURDATE(), INTERVAL 300 DAY)),
(2, 5, DATE_SUB(CURDATE(), INTERVAL 150 DAY)),
(2, 6, DATE_SUB(CURDATE(), INTERVAL 90 DAY));

INSERT INTO edicion_newsletter (titulo, resumen, fecha_publicacion) VALUES
('Panorama semanal de mercado', 'Resumen de tasas, dólar y Merval.', DATE_SUB(CURDATE(), INTERVAL 7 DAY)),
('Señales cuantitativas del mes', 'Resultados del modelo de momentum.', DATE_SUB(CURDATE(), INTERVAL 2 DAY));

INSERT INTO edicion_pack (id_edicion, id_pack) VALUES
(1, 1), (1, 2), (1, 3),
(2, 2), (2, 3);

-- Solo se registran envíos a alumnos con acceso vigente al pack de la edición
INSERT INTO envio (id_edicion, id_alumno, fecha_envio) VALUES
(1, 1, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(1, 2, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(1, 4, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(1, 5, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(2, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 2, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 4, DATE_SUB(NOW(), INTERVAL 2 DAY));
