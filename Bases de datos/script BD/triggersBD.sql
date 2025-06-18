DELIMITER $
CREATE TRIGGER `mydb`.`contar_inasistencias_aprendiz` 
AFTER INSERT ON `mydb`.`registro_asistencia` 
FOR EACH ROW 
BEGIN   
    DECLARE total_fallas INT;
    DECLARE coordinacion_aprendiz INT;
    
    -- Contar cuántas faltas ('F') tiene el aprendiz
    SELECT COUNT(*) INTO total_fallas   
    FROM `mydb`.`registro_asistencia`   
    WHERE aprendiz_id = NEW.aprendiz_id AND estado_asistencia = 'F';
    
    -- Obtener la coordinación del aprendiz a través de su programa
    SELECT p.coordinacion_id INTO coordinacion_aprendiz
    FROM `mydb`.`aprendiz` a
    INNER JOIN `mydb`.`programas` p ON a.programas_id_programas = p.id_programas
    WHERE a.id_aprendiz = NEW.aprendiz_id;
    
    -- Si tiene 5 o más faltas, gestionar la alerta
    IF total_fallas >= 5 THEN     
        -- Verificar si ya existe una alerta para este aprendiz
        IF EXISTS (       
            SELECT 1 FROM `mydb`.`alertas_inasistencia`       
            WHERE aprendiz_id = NEW.aprendiz_id     
        ) THEN       
            -- Actualizar la alerta existente
            UPDATE `mydb`.`alertas_inasistencia`       
            SET cantidad_fallas = total_fallas,           
                fecha_alerta = NOW(),           
                mensaje = CONCAT('⚠️ ALERTA CRÍTICA: El aprendiz con ID ', NEW.aprendiz_id, ' tiene ', total_fallas, ' inasistencias. Se debe iniciar proceso de deserción académica según reglamento SENA.')       
            WHERE aprendiz_id = NEW.aprendiz_id;      
        ELSE       
            -- Crear nueva alerta
            INSERT INTO `mydb`.`alertas_inasistencia` (
                aprendiz_id, 
                cantidad_fallas, 
                fecha_alerta,
                mensaje,
                coordinacion_id
            )       
            VALUES (         
                NEW.aprendiz_id,         
                total_fallas,
                NOW(),         
                CONCAT('⚠️ ALERTA CRÍTICA: El aprendiz con ID ', NEW.aprendiz_id, ' tiene ', total_fallas, ' inasistencias. Se debe iniciar proceso de deserción académica según reglamento SENA.'),
                IFNULL(coordinacion_aprendiz, 1)
            );     
        END IF;
        
    -- Si tiene 3 o 4 faltas, crear/actualizar alerta de advertencia
    ELSEIF total_fallas >= 3 THEN
        IF EXISTS (       
            SELECT 1 FROM `mydb`.`alertas_inasistencia`       
            WHERE aprendiz_id = NEW.aprendiz_id     
        ) THEN       
            -- Actualizar la alerta existente
            UPDATE `mydb`.`alertas_inasistencia`       
            SET cantidad_fallas = total_fallas,           
                fecha_alerta = NOW(),           
                mensaje = CONCAT('⚠️ ADVERTENCIA: El aprendiz con ID ', NEW.aprendiz_id, ' tiene ', total_fallas, ' inasistencias. Realizar seguimiento y citación.')       
            WHERE aprendiz_id = NEW.aprendiz_id;      
        ELSE       
            -- Crear nueva alerta de advertencia
            INSERT INTO `mydb`.`alertas_inasistencia` (
                aprendiz_id, 
                cantidad_fallas, 
                fecha_alerta,
                mensaje,
                coordinacion_id
            )       
            VALUES (         
                NEW.aprendiz_id,         
                total_fallas,
                NOW(),         
                CONCAT('⚠️ ADVERTENCIA: El aprendiz con ID ', NEW.aprendiz_id, ' tiene ', total_fallas, ' inasistencias. Realizar seguimiento y citación.'),
                IFNULL(coordinacion_aprendiz, 1)
            );     
        END IF;
    END IF;   
END$  
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
