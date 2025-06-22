DELIMITER $$

CREATE TRIGGER contar_inasistencias_aprendiz
AFTER INSERT ON registro_asistencia
FOR EACH ROW
BEGIN
    DECLARE total_fallas INT;
    DECLARE coordinacion_aprendiz INT;

    -- Contar cuántas inasistencias ('N') tiene el aprendiz
    SELECT COUNT(*) INTO total_fallas
    FROM registro_asistencia
    WHERE aprendiz_Usuario_id_usuario = NEW.aprendiz_Usuario_id_usuario
      AND estado_asistencia = 'N';

    -- Obtener la coordinación del aprendiz a través de su programa
    SELECT p.coordinacion_id INTO coordinacion_aprendiz
    FROM aprendiz a
    INNER JOIN programas p ON a.programas_id_programas = p.id_programas
    WHERE a.Usuario_id_usuario = NEW.aprendiz_Usuario_id_usuario;

    -- Si tiene 5 o más inasistencias, alerta crítica
    IF total_fallas >= 5 THEN
        IF EXISTS (
            SELECT 1 FROM alertas_inasistencia
            WHERE aprendiz_id = NEW.aprendiz_Usuario_id_usuario
        ) THEN
            UPDATE alertas_inasistencia
            SET cantidad_fallas = total_fallas,
                fecha_alerta = NOW(),
                mensaje = CONCAT('⚠️ ALERTA CRÍTICA: El aprendiz con ID ', NEW.aprendiz_Usuario_id_usuario, ' tiene ', total_fallas, ' inasistencias. Se debe iniciar proceso de deserción académica según reglamento SENA.')
            WHERE aprendiz_id = NEW.aprendiz_Usuario_id_usuario;
        ELSE
            INSERT INTO alertas_inasistencia (
                id_alerta,
                aprendiz_id,
                cantidad_fallas,
                fecha_alerta,
                mensaje,
                coordinacion_id
            )
            VALUES (
                NULL,
                NEW.aprendiz_Usuario_id_usuario,
                total_fallas,
                NOW(),
                CONCAT('⚠️ ALERTA CRÍTICA: El aprendiz con ID ', NEW.aprendiz_Usuario_id_usuario, ' tiene ', total_fallas, ' inasistencias. Se debe iniciar proceso de deserción académica según reglamento SENA.'),
                IFNULL(coordinacion_aprendiz, 1)
            );
        END IF;

    -- Si tiene entre 3 y 4 inasistencias, advertencia
    ELSEIF total_fallas >= 3 THEN
        IF EXISTS (
            SELECT 1 FROM alertas_inasistencia
            WHERE aprendiz_id = NEW.aprendiz_Usuario_id_usuario
        ) THEN
            UPDATE alertas_inasistencia
            SET cantidad_fallas = total_fallas,
                fecha_alerta = NOW(),
                mensaje = CONCAT('⚠️ ADVERTENCIA: El aprendiz con ID ', NEW.aprendiz_Usuario_id_usuario, ' tiene ', total_fallas, ' inasistencias. Realizar seguimiento y citación.')
            WHERE aprendiz_id = NEW.aprendiz_Usuario_id_usuario;
        ELSE
            INSERT INTO alertas_inasistencia (
                id_alerta,
                aprendiz_id,
                cantidad_fallas,
                fecha_alerta,
                mensaje,
                coordinacion_id
            )
            VALUES (
                NULL,
                NEW.aprendiz_Usuario_id_usuario,
                total_fallas,
                NOW(),
                CONCAT('⚠️ ADVERTENCIA: El aprendiz con ID ', NEW.aprendiz_Usuario_id_usuario, ' tiene ', total_fallas, ' inasistencias. Realizar seguimiento y citación.'),
                IFNULL(coordinacion_aprendiz, 1)
            );
        END IF;
    END IF;
END$$

DELIMITER ;


DELIMITER //

CREATE TRIGGER tr_asignar_estado_minuta
BEFORE INSERT ON registro_minuta
FOR EACH ROW
BEGIN
    DECLARE ahora DATETIME;
    SET ahora = NOW();

    IF (ahora BETWEEN NEW.fecha_hora_recibo AND NEW.fecha_hora_entrega) THEN
        UPDATE ambiente
        SET estado = 'Ocupado'
        WHERE id_ambiente = NEW.ambiente_id;
    ELSE
        UPDATE ambiente
        SET estado = 'Disponible'
        WHERE id_ambiente = NEW.ambiente_id;
    END IF;
END //

DELIMITER ;



DELIMITER //

CREATE EVENT IF NOT EXISTS actualizar_minutas_a_disponible
ON SCHEDULE EVERY 1 MINUTE
DO
BEGIN
    UPDATE registro_minuta
    SET estado = 'Disponible'
    WHERE estado = 'Ocupado'
      AND NOW() > fecha_hora_entrega;
END //

DELIMITER ;
