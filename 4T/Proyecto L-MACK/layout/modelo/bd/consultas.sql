-- 1. Cantidad de aprendices por ficha y programa
SELECT 
    f.Num_ficha,
    COUNT(a.Usuario_id_usuario) AS cantidad_aprendices,
    p.nombre_programa,
    j.nombre_jornada,
    m.nombre_modalidad
FROM aprendiz a
JOIN ficha f ON a.ficha_idficha = f.idficha
JOIN programas p ON a.programas_id_programas = p.id_programas
JOIN jornada j ON p.jornada_id = j.id_jornada
JOIN modalidad m ON p.modalidad_id = m.id_modalidad
GROUP BY f.Num_ficha, p.nombre_programa, j.nombre_jornada, m.nombre_modalidad
ORDER BY cantidad_aprendices DESC;

-- 2. Ambientes disponibles y responsable de la última minuta
SELECT 
    a.num_ambiente,
    a.tipo_ambiente,
    CONCAT(u.p_nombre, ' ', u.p_apellido) AS responsable_minuta,
    MAX(rm.fecha_hora_recibo) AS ultima_fecha_minuta
FROM ambiente a
JOIN registro_minuta rm ON rm.ambiente_id = a.id_ambiente
JOIN usuario u ON rm.responsable_id = u.id_usuario
WHERE a.estado = 'Disponible'
GROUP BY a.id_ambiente, a.num_ambiente, a.tipo_ambiente, u.p_nombre, u.p_apellido
ORDER BY ultima_fecha_minuta DESC;

-- 3. Cantidad de incidentes registrados por ambiente
SELECT 
    a.num_ambiente,
    a.tipo_ambiente,
    COUNT(ri.id_incidente) AS total_incidentes,
    GROUP_CONCAT(DISTINCT ti.tipo_incidente SEPARATOR ', ') AS tipos_incidentes_reportados
FROM ambiente a
LEFT JOIN registro_incidente ri ON ri.ambiente_id = a.id_ambiente
LEFT JOIN tipo_incidente ti ON ri.tipo_inc_id = ti.id_tipo_inc
GROUP BY a.id_ambiente, a.num_ambiente, a.tipo_ambiente
ORDER BY total_incidentes DESC;

-- 4. Programas con recursos disponibles en sus ambientes
SELECT 
    a.num_ambiente,
    a.tipo_ambiente,
    a.capacidad,
    COUNT(r.id_recurso) AS total_recursos,
    SUM(r.estado = 'Disponible') AS recursos_disponibles,
    GROUP_CONCAT(DISTINCT r.nombre_recurso) AS nombres_disponibles,
    COUNT(ri.id_incidente) AS total_incidentes,
    MAX(ri.fecha_incidente) AS ultimo_incidente,
    GROUP_CONCAT(DISTINCT CONCAT(u.p_nombre, ' ', u.p_apellido)) AS quienes_reportaron
FROM ambiente a
LEFT JOIN recursos r ON r.ambiente_id = a.id_ambiente AND r.estado = 'Disponible'
LEFT JOIN registro_incidente ri ON ri.ambiente_id = a.id_ambiente
LEFT JOIN usuario u ON u.id_usuario = ri.usuario_id_usuario
GROUP BY a.id_ambiente
ORDER BY total_incidentes DESC;




-- 5. Resumen de asistencia por aprendiz y jornada
SELECT 
    CONCAT(u.p_nombre, ' ', IFNULL(u.s_nombre, ''), ' ', u.p_apellido) AS nombre_completo,
    j.nombre_jornada,
    COUNT(ri.id_inasistencia) AS total_registros,
    SUM(CASE WHEN ri.estado_inasistencia = 'S' THEN 1 ELSE 0 END) AS total_asistencias,
    ROUND(SUM(CASE WHEN ri.estado_inasistencia = 'S' THEN 1 ELSE 0 END) * 100.0 / COUNT(ri.id_inasistencia), 2) AS porcentaje_asistencia,
    MAX(ri.fecha_inasistencia) AS ultima_asistencia
FROM registro_inasistencia ri
JOIN aprendiz a ON ri.aprendiz_Usuario_id_usuario = a.Usuario_id_usuario
JOIN usuario u ON a.Usuario_id_usuario = u.id_usuario
JOIN programas p ON a.programas_id_programas = p.id_programas
JOIN jornada j ON p.jornada_id = j.id_jornada
GROUP BY nombre_completo, j.nombre_jornada;
