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
