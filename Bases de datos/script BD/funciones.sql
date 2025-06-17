-- Función 1: obtener estado ambiente
DELIMITER //
CREATE FUNCTION estado_ambiente(id INT)
RETURNS VARCHAR(30)
READS SQL DATA
BEGIN
    DECLARE estado_actual VARCHAR(30);
    SELECT estado INTO estado_actual FROM ambiente WHERE id_ambiente = id;
    RETURN estado_actual;
END //
DELIMITER ;

-- Función 2: verificar recursos por ambiente
DELIMITER //
CREATE FUNCTION total_recursos_ambiente(id INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total FROM recursos WHERE ambiente_id = id;
    RETURN total;
END //
DELIMITER ;

-- Función 3: obtener incidentes por ambiente
DELIMITER //
CREATE FUNCTION contar_incidentes_ambiente(id INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total FROM registro_incidente WHERE ambiente_id = id;
    RETURN total;
END //
DELIMITER ;

-- Función 4: contar aprendices programa
DELIMITER //
CREATE FUNCTION contar_aprendices_programa(id_programa INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total FROM aprendiz WHERE programa_id = id_programa;
    RETURN total;
END //
DELIMITER ;
