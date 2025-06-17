DELIMITER $$

CREATE TRIGGER verificar_inasistencias
AFTER INSERT ON registro_asistencia
FOR EACH ROW
BEGIN
  DECLARE total_fallas INT;

  -- Contar inasistencias del aprendiz
  SELECT COUNT(*) INTO total_fallas
  FROM registro_asistencia
  WHERE apr_id = NEW.apr_id AND estado_asistencia = 'N';

  -- Si llegó a 5, mostrar alerta
  IF total_fallas = 5 THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'El aprendiz ya tiene 5 inasistencias. Debe iniciarse el proceso de deserción.';
  END IF;
END$$

DELIMITER ;
