-- 1. Aprendices con su programa, jornada y modalidad
SELECT a.p_nombre, a.p_apellido, p.nombre_programa, j.nombre_jornada, m.nombre_modalidad
FROM aprendiz a
JOIN programas p ON a.programa_id = p.id_programas
JOIN jornada j ON p.jornada_id = j.id_jornada
JOIN modalidad m ON p.modalidad_id = m.id_modalidad;

-- 2. Ambientes, coordinación encargada y responsable de la última minuta

SELECT amb.num_ambiente, amb.tipo_ambiente, c.nombre_coordinacion, u.nombre AS responsable
FROM ambiente amb
JOIN coordinacion c ON amb.coordinacion_id = c.id_coordinacion
JOIN registro_minuta rm ON rm.ambiente_id = amb.id_ambiente
JOIN Usuario u ON u.documento = rm.docu_id;

--  3. Incidentes registrados con su tipo, ambiente y coordinación

SELECT ri.descripcion, ti.tipo_incidente, amb.num_ambiente, c.nombre_coordinacion
FROM registro_incidente ri
JOIN tipo_incidente ti ON ri.tipo_inc_id = ti.id_tipo_inc
JOIN ambiente amb ON ri.ambiente_id = amb.id_ambiente
JOIN coordinacion c ON amb.coordinacion_id = c.id_coordinacion;


-- 4. Programas con sus coordinaciones, ambientes disponibles y recursos operativos

SELECT p.nombre_programa, c.nombre_coordinacion, amb.num_ambiente, r.serial_recurso
FROM programas p
JOIN coordinacion c ON p.coordinacion_id = c.id_coordinacion
JOIN ambiente amb ON amb.coordinacion_id = c.id_coordinacion
JOIN recursos r ON r.ambiente_id = amb.id_ambiente
WHERE amb.estado = 'Disponible' AND r.estado = 'Operativo';

-- 5. Asistencias registradas por jornada y aprendiz con nombre completo

SELECT CONCAT(a.p_nombre, ' ', a.s_nombre, ' ', a.p_apellido) AS nombre_completo,
       j.nombre_jornada, ra.fecha_asistencia, ra.estado_asistencia
FROM registro_asistencia ra
JOIN aprendiz a ON ra.apr_id = a.id_aprendiz
JOIN jornada j ON ra.jorn_id = j.id_jornada;




