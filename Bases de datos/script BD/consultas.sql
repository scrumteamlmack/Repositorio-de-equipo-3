use mydb;
-- 1. Aprendices con su programa, jornada y modalidad
SELECT u.p_nombre, u.p_apellido, 
       p.nombre_programa, 
       j.nombre_jornada, 
       m.nombre_modalidad
FROM aprendiz a
JOIN Usuario u ON a.Usuario_id_usuario = u.id_usuario
JOIN programas p ON a.programas_id_programas = p.id_programas
JOIN jornada j ON a.jornada_id_jornada = j.id_jornada
JOIN modalidad m ON p.modalidad_id = m.id_modalidad;


-- 2. Ambientes, coordinación encargada y responsable de la última minuta

SELECT a.num_ambiente, a.tipo_ambiente, 
       c.nombre_coordinacion, 
       u.p_nombre AS responsable, 
       rm.fecha_hora_recibo
FROM ambiente a
JOIN coordinacion c ON a.coordinacion_id = c.id_coordinacion
JOIN registro_minuta rm ON rm.ambiente_id = a.id_ambiente
JOIN Usuario u ON u.id_usuario = rm.Usuario_id_usuario;


--  3. Incidentes registrados con su tipo, ambiente y coordinación

SELECT ri.descripcion, 
       ti.tipo_incidente, 
       a.num_ambiente, 
       c.nombre_coordinacion
FROM registro_incidente ri
JOIN tipo_incidente ti ON ri.tipo_inc_id = ti.id_tipo_inc
JOIN ambiente a ON ri.ambiente_id = a.id_ambiente
JOIN coordinacion c ON a.coordinacion_id = c.id_coordinacion;



-- 4. Programas con sus coordinaciones, ambientes disponibles y recursos operativos

SELECT p.nombre_programa, 
       c.nombre_coordinacion, 
       a.num_ambiente, 
       r.serial_recurso
FROM programas p
JOIN coordinacion c ON p.coordinacion_id = c.id_coordinacion
JOIN ambiente a ON a.coordinacion_id = c.id_coordinacion
JOIN recursos r ON r.ambiente_id = a.id_ambiente
WHERE a.estado = 'Disponible' ;


-- 5. Asistencias registradas por jornada y aprendiz con nombre completo

SELECT CONCAT(u.p_nombre, ' ', u.s_nombre, ' ', u.p_apellido) AS nombre_completo,
       j.nombre_jornada, 
       ra.fecha_asistencia, 
       ra.estado_asistencia
FROM registro_asistencia ra
JOIN aprendiz a ON ra.aprendiz_Usuario_id_usuario = a.Usuario_id_usuario
JOIN Usuario u ON a.Usuario_id_usuario = u.id_usuario
JOIN jornada j ON ra.jornada_id = j.id_jornada;





