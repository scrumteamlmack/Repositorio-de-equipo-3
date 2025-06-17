-- Procedimiento 1: control de la minuta
DELIMITER //
CREATE PROCEDURE control_minuta(
    IN fecha_recibido DATETIME,
    IN fecha_entrega DATETIME,
    IN novedad TEXT,
    IN responsable VARCHAR(30),
    IN descripcion TEXT,
    IN estado varchar(15),
    IN ambiente_id INT,
    IN documento_usuario INT
)
BEGIN
    INSERT INTO registro_minuta(fecha_hora_recibido, fecha_hora_entrega, novedad, responsable, descripcion_amb, ambiente_id, docu_id)
    VALUES (fecha_recibido, fecha_entrega, novedad, responsable, descripcion, ambiente_id, documento_usuario);
END //
DELIMITER ;

-- Procedimiento 2: registrar ingreso aprendiz
DELIMITER //
CREATE PROCEDURE registrar_ingreso_aprendiz(
    IN fecha DATE,
    IN estado ENUM('N', 'E', 'S'),
    IN id_jornada INT,
    IN id_aprendiz INT
)
BEGIN
    INSERT INTO registro_asistencia(fecha_asistencia, estado_asistencia, jorn_id, apr_id)
    VALUES (fecha, estado, id_jornada, id_aprendiz);
END //
DELIMITER ;


-- Procedimiento 3: registrar incidente
DELIMITER //
CREATE PROCEDURE registrar_incidente(
    IN descripcion TEXT,
    IN fecha DATE,
    IN hora TIME,
    IN id_ambiente INT,
    IN id_tipo INT
)
BEGIN
    INSERT INTO registro_incidente(descripcion, fecha_incidente, hora_incidente, ambiente_id, tipo_inc_id)
    VALUES (descripcion, fecha, hora, id_ambiente, id_tipo);
END //
DELIMITER ;

-- Procedimiento 4: asignar recursos a ambiente
DELIMITER //
CREATE PROCEDURE asignar_recurso_ambiente(
    IN serial VARCHAR(30),
    IN numero TINYINT,
    IN estado VARCHAR(20),
    IN observacion TEXT,
    IN ambiente INT,
    IN tipo INT
)
BEGIN
    INSERT INTO recursos(serial_recurso, num_recurso, estado, observacion, ambiente_id, recurso_id)
    VALUES (serial, numero, estado, observacion, ambiente, tipo);
END //
DELIMITER ;

-- Procedimiento 5: actualizar estado recurso
DELIMITER //
CREATE PROCEDURE actualizar_estado_recurso(
    IN serial VARCHAR(30),
    IN nuevo_estado VARCHAR(20)
)
BEGIN
    UPDATE recursos
    SET estado = nuevo_estado
    WHERE serial_recurso = serial;
END //
DELIMITER ;

-- Procedimiento 6: mostrar ambientes disponibles
DELIMITER //
CREATE PROCEDURE ambientes_disponibles()
BEGIN
    SELECT * FROM ambiente WHERE estado = 'disponible';
END //
DELIMITER ;

-- Procedimiento 7: cambiar responsable minuta
DELIMITER //
CREATE PROCEDURE actualizar_responsable_minuta(
    IN id_minuta INT,
    IN nuevo_responsable VARCHAR(30)
)
BEGIN
    UPDATE registro_minuta
    SET responsable = nuevo_responsable
    WHERE id_minuta = id_minuta;
END //
DELIMITER ;

-- Procedimiento 8: reporte de recursos de ambiente
DELIMITER //
CREATE PROCEDURE reporte_recursos_ambiente(
    IN id_ambiente INT
)
BEGIN
    SELECT r.serial_recurso, tr.recurso_tipo, r.estado, r.observacion
    FROM recursos r
    JOIN tipo_recurso tr ON r.recurso_id = tr.id_recurso
    WHERE r.ambiente_id = id_ambiente;
END //
DELIMITER ;

