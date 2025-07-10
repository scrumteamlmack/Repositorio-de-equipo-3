use mydb;
SELECT 
    a.Num_ficha,
    COUNT(*) AS cantidad_aprendices,
    p.nombre_programa,
    j.nombre_jornada,
    m.nombre_modalidad
FROM aprendiz a
JOIN programas p ON a.programas_id_programas = p.id_programas
JOIN jornada j ON a.jornada_id_jornada = j.id_jornada
JOIN modalidad m ON p.modalidad_id = m.id_modalidad
GROUP BY a.Num_ficha, p.nombre_programa, j.nombre_jornada, m.nombre_modalidad
ORDER BY cantidad_aprendices DESC;



-- 2. Ambientes, coordinación encargada y responsable de la última minuta

SELECT 
    a.num_ambiente,
    a.tipo_ambiente,
    c.nombre_coordinacion,
    COUNT(r.id_recurso) AS total_recursos_disponibles,
    CONCAT(u.p_nombre, ' ', u.p_apellido) AS responsable_minuta,
    MAX(rm.fecha_hora_recibo) AS ultima_fecha_minuta
FROM ambiente a
JOIN coordinacion c ON a.coordinacion_id = c.id_coordinacion
JOIN recursos r ON r.ambiente_id = a.id_ambiente
JOIN registro_minuta rm ON rm.ambiente_id = a.id_ambiente
JOIN Usuario u ON u.id_usuario = rm.Usuario_id_usuario
WHERE r.estado IN ('Disponible')
GROUP BY a.num_ambiente, a.tipo_ambiente, c.nombre_coordinacion, responsable_minuta;





--  3. Conteo de incidentes registrados por ambiente y tipos reportados

SELECT 
    a.num_ambiente,
    a.tipo_ambiente,
    c.nombre_coordinacion,
    COUNT(ri.id_incidente) AS total_incidentes,
    GROUP_CONCAT(DISTINCT ti.tipo_incidente SEPARATOR ', ') AS tipos_incidentes_reportados
FROM ambiente a
JOIN coordinacion c ON a.coordinacion_id = c.id_coordinacion
LEFT JOIN registro_incidente ri ON ri.ambiente_id = a.id_ambiente
LEFT JOIN tipo_incidente ti ON ri.tipo_inc_id = ti.id_tipo_inc
GROUP BY a.id_ambiente, a.num_ambiente, a.tipo_ambiente, c.nombre_coordinacion
ORDER BY total_incidentes DESC;




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

SELECT 
    CONCAT(u.p_nombre, ' ', u.s_nombre, ' ', u.p_apellido) AS nombre_completo,
    j.nombre_jornada,
    COUNT(ra.id_asistencia) AS total_registros,
    SUM(CASE WHEN ra.estado_asistencia = 'S' THEN 1 ELSE 0 END) AS total_asistencias,
    ROUND(SUM(CASE WHEN ra.estado_asistencia = 'S' THEN 1 ELSE 0 END) * 100.0 / COUNT(ra.id_asistencia), 2) AS porcentaje_asistencia,
    MAX(ra.fecha_asistencia) AS ultima_asistencia
FROM registro_asistencia ra
JOIN aprendiz a ON ra.aprendiz_Usuario_id_usuario = a.Usuario_id_usuario
JOIN Usuario u ON a.Usuario_id_usuario = u.id_usuario
JOIN jornada j ON ra.jornada_id = j.id_jornada
GROUP BY nombre_completo, j.nombre_jornada;







