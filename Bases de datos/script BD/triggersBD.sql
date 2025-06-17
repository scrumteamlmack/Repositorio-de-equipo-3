DELIMITER $$

CREATE TRIGGER contar_inasistencias_aprendiz
AFTER INSERT ON registro_asistencia
FOR EACH ROW
BEGIN
  DECLARE total_fallas INT;

  -- Contar cuántas inasistencias ('N') tiene el aprendiz
  SELECT COUNT(*) INTO total_fallas
  FROM registro_asistencia
  WHERE apr_id = NEW.apr_id AND estado_asistencia = 'N';

  -- Si tiene 5 o más, actualiza o inserta alerta
  IF total_fallas >= 5 THEN
    -- Si ya existe una alerta para este aprendiz
    IF EXISTS (
      SELECT 1 FROM alertas_inasistencia
      WHERE aprendiz_id = NEW.apr_id
    ) THEN
      -- Solo actualiza la cantidad de fallas y fecha
      UPDATE alertas_inasistencia
      SET cantidad_fallas = total_fallas,
          fecha_alerta = NOW(),
          mensaje = CONCAT('⚠️ Atención: El aprendiz con ID ', NEW.apr_id, ' tiene ', total_fallas, ' inasistencias. Iniciar proceso de deserción.')
      WHERE aprendiz_id = NEW.apr_id;

    ELSE
      -- Si no existe aún, crea la alerta
      INSERT INTO alertas_inasistencia (aprendiz_id, cantidad_fallas, mensaje)
      VALUES (
        NEW.apr_id,
        total_fallas,
        CONCAT('⚠️ Atención: El aprendiz con ID ', NEW.apr_id, ' tiene ', total_fallas, ' inasistencias. Iniciar proceso de deserción.')
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

    IF (ahora BETWEEN NEW.fecha_hora_recibido AND NEW.fecha_hora_entrega) THEN
        SET NEW.estado = 'Ocupado';
    ELSE
        SET NEW.estado = 'Disponible';
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
