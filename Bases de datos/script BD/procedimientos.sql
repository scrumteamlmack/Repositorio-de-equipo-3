DELIMITER //

CREATE PROCEDURE control_minuta(
    IN fecha_recibido DATETIME,
    IN fecha_entrega DATETIME,
    IN novedad TEXT,
    IN responsable VARCHAR(250),
    IN descripcion TEXT,
    IN ambiente_id INT,
    IN documento_usuario INT,
    IN id_guardia INT
)
BEGIN
    INSERT INTO registro_minuta(
        fecha_hora_recibo, fecha_hora_entrega, novedad,
        responsable, descripcion_min, ambiente_id,
        Usuario_id_usuario, guarda_seguridad_Usuario_id_usuario
    )
    VALUES (
        fecha_recibido, fecha_entrega, novedad,
        responsable, descripcion, ambiente_id,
        documento_usuario, id_guardia
    );
END //

DELIMITER ;


DELIMITER //
CREATE PROCEDURE consultar_asistencia_aprendiz(
    IN id_aprendiz INT
)
BEGIN
    SELECT fecha_asistencia, estado_asistencia
    FROM registro_asistencia
    WHERE aprendiz_Usuario_id_usuario = id_aprendiz;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE eliminar_incidente(
    IN id_incidente INT
)
BEGIN
    DELETE FROM registro_incidente
    WHERE id_incidente = id_incidente;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE actualizar_estado_recurso(
    IN id_recurso INT,
    IN nuevo_estado VARCHAR(20)
)
BEGIN
    UPDATE recursos
    SET estado = nuevo_estado
    WHERE id_recurso = id_recurso;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE consultar_recursos_ambiente(
    IN id_ambiente INT
)
BEGIN
    SELECT r.serial_recurso, r.nombre_recurso, tr.recurso_tipo, r.estado, r.observacion
    FROM recursos r
    JOIN tipo_recurso tr ON r.tipo_recurso = tr.id_tipo_recurso
    WHERE r.ambiente_id = id_ambiente;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE consultar_ambientes_disponibles()
BEGIN
    SELECT * FROM ambiente
    WHERE estado = 'Disponible';
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE actualizar_responsable_minuta(
    IN id_min INT,
    IN nuevo_responsable VARCHAR(250)
)
BEGIN
    UPDATE registro_minuta
    SET responsable = nuevo_responsable
    WHERE id_minuta = id_min;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE eliminar_recurso(
    IN id_recurso INT
)
BEGIN
    DELETE FROM recursos
    WHERE id_recurso = id_recurso;
END //
DELIMITER ;
