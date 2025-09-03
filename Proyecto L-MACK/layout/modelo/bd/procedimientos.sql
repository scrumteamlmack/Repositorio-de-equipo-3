-- 1. Consultar inasistencias del aprendiz (estado 'N' o más de una 'R')
DELIMITER //
CREATE PROCEDURE ver_inasistencias_aprendiz(
    IN id_aprendiz INT
)
BEGIN
    DECLARE total_faltas INT;

    -- Contar cuántas inasistencias no justificadas tiene
    SELECT COUNT(*) INTO total_faltas
    FROM registro_inasistencia
    WHERE aprendiz_Usuario_id_usuario = id_aprendiz AND estado_inasistencia = 'N';

    -- Si tiene al menos una, mostrar la lista
    IF total_faltas > 0 THEN
        SELECT 
            u.p_nombre AS nombre,
            u.p_apellido AS apellido,
            ri.fecha_inasistencia,
            ri.estado_inasistencia
        FROM registro_inasistencia ri
        INNER JOIN usuario u ON u.id_usuario = ri.aprendiz_Usuario_id_usuario
        WHERE ri.aprendiz_Usuario_id_usuario = id_aprendiz
          AND ri.estado_inasistencia = 'N';
    ELSE
        SELECT 'El aprendiz no ha faltado a ninguna formación.' AS mensaje;
    END IF;
END //
DELIMITER ;



-- 2. Eliminar un incidente
DELIMITER //
CREATE PROCEDURE eliminar_incidente(
    IN id_inc INT
)
BEGIN
    DELETE FROM registro_incidente
    WHERE id_incidente = id_inc;
END //
DELIMITER ;

-- 3. Actualizar estado de un recurso
DELIMITER //
CREATE PROCEDURE actualizar_estado_recurso(
    IN recurso_id INT,
    IN nuevo_estado VARCHAR(50)
)
BEGIN
    UPDATE recursos
    SET estado = nuevo_estado
    WHERE id_recurso = recurso_id;
END //
DELIMITER ;

-- 4. Consultar recursos de un ambiente
DELIMITER //
CREATE PROCEDURE consultar_recursos_ambiente(
    IN id_ambiente INT
)
BEGIN
    SELECT r.id_recurso, r.nombre_recurso, r.estado, tr.recurso_tipo
    FROM recursos r
    INNER JOIN tipo_recurso tr ON r.tipo_recurso = tr.id_tipo_recurso
    WHERE r.ambiente_id = id_ambiente;
END //
DELIMITER ;

-- 5. Eliminar recurso (opcional si prefieres desactivar con estado)
DELIMITER //
CREATE PROCEDURE eliminar_recurso(
    IN recurso_id INT
)
BEGIN
    DELETE FROM recursos
    WHERE id_recurso = recurso_id;
END //
DELIMITER ;

-- 6. Registrar minuta de control
DELIMITER //
CREATE PROCEDURE control_minuta(
    IN fecha_recibido DATETIME,
    IN fecha_entrega DATETIME,
    IN novedad TEXT,
    IN descripcion TEXT,
    IN ambiente_id INT,
    IN id_guardia INT,
    IN id_responsable INT
    
)
BEGIN
    INSERT INTO registro_minuta(
        fecha_hora_recibo, fecha_hora_entrega, novedad,
        descripcion_min, ambiente_id,
        guarda_seguridad_Usuario_id_usuario, responsable_id
    )
    VALUES (
        fecha_recibido, fecha_entrega, novedad,
        descripcion, ambiente_id,
        id_guardia, id_responsable
    );
END //
DELIMITER ;



-- 7. Monitorear traslado de recurso (incluye inserción y actualización del recurso)
DELIMITER //
CREATE PROCEDURE monitorear_traslado_recurso(
    IN recurso_id INT,
    IN ambiente_origen INT,
    IN ambiente_destino INT,
    IN observacion TEXT
)
BEGIN
    INSERT INTO traslado_recurso (
        recurso_id, ambiente_origen, ambiente_destino,
        fecha_traslado, observacion
    )
    VALUES (
        recurso_id, ambiente_origen, ambiente_destino,
        NOW(), observacion
    );

    UPDATE recursos
    SET ambiente_id = ambiente_destino
    WHERE id_recurso = recurso_id;
END //
DELIMITER ;
