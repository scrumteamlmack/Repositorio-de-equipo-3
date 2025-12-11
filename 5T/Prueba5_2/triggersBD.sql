USE mydb;

-----------------------------------------------------------
-- TRIGGER 1: CONTAR INASISTENCIAS DEL APRENDIZ
-- Adaptado a tabla registro_inasistencia y tus columnas reales
-----------------------------------------------------------

DELIMITER $$

CREATE TRIGGER contar_inasistencias_aprendiz
AFTER INSERT ON registro_inasistencia
FOR EACH ROW
BEGIN
    DECLARE total_fallas INT;
    DECLARE coordinacion_aprendiz INT;

    -- Contar inasistencias ('N')
    SELECT COUNT(*) INTO total_fallas
    FROM registro_inasistencia
    WHERE aprendiz_Usuario_id_usuario = NEW.aprendiz_Usuario_id_usuario
      AND estado_inasistencia = 'N';

    -- Obtener la coordinación del aprendiz
    SELECT p.coordinacion_id INTO coordinacion_aprendiz
    FROM aprendiz a
    INNER JOIN programas p 
        ON a.programas_id_programas = p.id_programas
    WHERE a.Usuario_id_usuario = NEW.aprendiz_Usuario_id_usuario;

    -- Si tiene 5 o más fallas, generar o actualizar alerta
    IF total_fallas >= 5 THEN
        
        IF EXISTS (
            SELECT 1 FROM alertas_inasistencia
            WHERE aprendiz_id = NEW.aprendiz_Usuario_id_usuario
        ) THEN
            UPDATE alertas_inasistencia
            SET cantidad_fallas = total_fallas,
                fecha_alerta = NOW(),
                mensaje = CONCAT(
                    '⚠️ ALERTA CRÍTICA: El aprendiz con ID ',
                    NEW.aprendiz_Usuario_id_usuario,
                    ' tiene ',
                    total_fallas,
                    ' inasistencias. Se debe iniciar proceso de deserción académica.'
                )
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
                CONCAT(
                    '⚠️ ALERTA CRÍTICA: El aprendiz con ID ',
                    NEW.aprendiz_Usuario_id_usuario,
                    ' tiene ',
                    total_fallas,
                    ' inasistencias. Se debe iniciar proceso de deserción académica.'
                ),
                IFNULL(coordinacion_aprendiz, 1)
            );
        END IF;

    END IF;

END$$

DELIMITER ;

-----------------------------------------------------------
-- TRIGGER 2: ESTADO AUTOMÁTICO DE MINUTA (OCUPADO / DISPONIBLE)
-----------------------------------------------------------

DELIMITER $$

CREATE TRIGGER tr_asignar_estado_minuta
BEFORE INSERT ON registro_minuta
FOR EACH ROW
BEGIN
    IF NEW.fecha_hora_entrega > NOW() THEN
        SET NEW.estado = 'Ocupado';
    ELSE
        SET NEW.estado = 'Disponible';
    END IF;
END$$

DELIMITER ;

-----------------------------------------------------------
-- EVENTO: ACTUALIZAR MINUTAS A "DISPONIBLE" AUTOMÁTICAMENTE
-----------------------------------------------------------

DELIMITER $$

CREATE EVENT IF NOT EXISTS actualizar_minutas_a_disponible
ON SCHEDULE EVERY 1 MINUTE
DO
BEGIN
    UPDATE registro_minuta
    SET estado = 'Disponible'
    WHERE estado = 'Ocupado'
      AND NOW() > fecha_hora_entrega;
END$$

DELIMITER ;

-----------------------------------------------------------
-- TRIGGER 3: REGISTRO AUTOMÁTICO DE HISTÓRICO DE INCIDENTES
-- Adaptado a estructura real de registro_incidente e historico_incidentes
-----------------------------------------------------------

DELIMITER $$

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
