-- ==========================================================
-- RESETEO DE TRIGGERS Y EVENTOS EXISTENTES
-- ==========================================================
DROP TRIGGER IF EXISTS contar_inasistencias_aprendiz;
DROP TRIGGER IF EXISTS tr_asignar_estado_minuta;
DROP TRIGGER IF EXISTS responsable_registro_incidente;
DROP EVENT IF EXISTS actualizar_minutas_a_disponible;

-- ==========================================================
-- CREACIÓN DE TRIGGERS Y EVENTOS CORREGIDOS
-- ==========================================================

DELIMITER $$

-- TRIGGER 1: Contar inasistencias de aprendiz y generar alerta
CREATE TRIGGER contar_inasistencias_aprendiz
AFTER INSERT ON registro_inasistencia
FOR EACH ROW
BEGIN
    DECLARE total_fallas INT;
    DECLARE coordinacion_aprendiz INT;

    -- Contar cuántas inasistencias ('N') tiene el aprendiz
    SELECT COUNT(*) INTO total_fallas
    FROM registro_inasistencia
    WHERE aprendiz_Usuario_id_usuario = NEW.aprendiz_Usuario_id_usuario
      AND estado_inasistencia = 'N';

    -- Obtener la coordinación del aprendiz a través de su programa
    SELECT p.coordinacion_id INTO coordinacion_aprendiz
    FROM aprendiz a
    INNER JOIN programas p ON a.programas_id_programas = p.id_programas
    WHERE a.Usuario_id_usuario = NEW.aprendiz_Usuario_id_usuario;

    -- Si tiene 5 o más inasistencias
    IF total_fallas >= 5 THEN
        IF EXISTS (
            SELECT 1 FROM alertas_inasistencia
            WHERE aprendiz_id = NEW.aprendiz_Usuario_id_usuario
        ) THEN
            UPDATE alertas_inasistencia
            SET cantidad_fallas = total_fallas,
                fecha_alerta = NOW(),
                mensaje = CONCAT('⚠️ ALERTA CRÍTICA: El aprendiz con ID ', NEW.aprendiz_Usuario_id_usuario, 
                                 ' tiene ', total_fallas, ' inasistencias. Se debe iniciar proceso de deserción académica.')
            WHERE aprendiz_id = NEW.aprendiz_Usuario_id_usuario;
        ELSE
            INSERT INTO alertas_inasistencia (
                aprendiz_id,
                cantidad_fallas,
                fecha_alerta,
                mensaje,
                coordinacion_id
            )
            VALUES (
                NEW.aprendiz_Usuario_id_usuario,
                total_fallas,
                NOW(),
                CONCAT('⚠️ ALERTA CRÍTICA: El aprendiz con ID ', NEW.aprendiz_Usuario_id_usuario, 
                       ' tiene ', total_fallas, ' inasistencias. Se debe iniciar proceso de deserción académica.'),
                IFNULL(coordinacion_aprendiz, 1)
            );
        END IF;
    END IF;
END$$


-- TRIGGER 2: Cambiar estado del ambiente según la minuta
CREATE TRIGGER tr_asignar_estado_minuta
BEFORE INSERT ON registro_minuta
FOR EACH ROW
BEGIN
    DECLARE ahora DATETIME;
    SET ahora = NOW();

    IF (ahora BETWEEN NEW.fecha_hora_recibo AND NEW.fecha_hora_entrega) THEN
        SET NEW.estado = 'Ocupado';
    ELSE
        SET NEW.estado = 'Disponible';
    END IF;
END$$


-- TRIGGER 3: Guardar histórico de incidentes
CREATE TRIGGER responsable_registro_incidente
AFTER INSERT ON registro_incidente
FOR EACH ROW
BEGIN
    INSERT INTO historico_incidentes (
        incidente_id,
        ambiente_id,
        tipo_incidente_id,
        descripcion,
        fecha_registro
    )
    VALUES (
        NEW.id_incidente,
        NEW.ambiente_id,
        NEW.tipo_inc_id,
        NEW.descripcion,
        NOW()
    );
END$$

DELIMITER ;


-- EVENTO: Actualizar automáticamente minutas vencidas a Disponible
DELIMITER //
CREATE EVENT IF NOT EXISTS actualizar_minutas_a_disponible
ON SCHEDULE EVERY 1 MINUTE
DO
    UPDATE registro_minuta
    SET estado = 'Disponible'
    WHERE estado = 'Ocupado'
      AND NOW() > fecha_hora_entrega;
//
DELIMITER ;
