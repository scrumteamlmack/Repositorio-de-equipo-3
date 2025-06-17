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


