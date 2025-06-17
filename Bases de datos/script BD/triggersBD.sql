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

  -- Si tiene 5 o más, registrar una alerta
  IF total_fallas >= 5 THEN
    INSERT INTO alertas_inasistencia (aprendiz_id, cantidad_fallas, mensaje)
    VALUES (
      NEW.apr_id,
      total_fallas,
      CONCAT('⚠️ Atención: El aprendiz con ID ', NEW.apr_id, ' tiene ', total_fallas, ' inasistencias. Iniciar proceso de deserción.')
    );
  END IF;
END$$

DELIMITER ;
