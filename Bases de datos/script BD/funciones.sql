DELIMITER //
CREATE FUNCTION estado_recursos_ambiente(id INT)
RETURNS TEXT
READS SQL DATA
BEGIN
    DECLARE resultado TEXT DEFAULT '';
    
    SELECT GROUP_CONCAT(CONCAT(nombre_recurso, ' (', estado, ')') SEPARATOR ', ')
    INTO resultado	
    FROM recursos
    WHERE ambiente_id = id;

    RETURN resultado;
END //
DELIMITER ;



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

-- Función 5: consultar programa del aprendiz
DELIMITER //
CREATE FUNCTION programa_aprendiz(id_aprendiz INT)
RETURNS VARCHAR(30)
READS SQL DATA
BEGIN
    DECLARE nombre_programa VARCHAR(50);
    SELECT p.nombre_programa INTO nombre_programa
    FROM aprendiz a JOIN programas p ON a.programa_id = p.id_programas
    WHERE a.id_aprendiz = id_aprendiz;
    RETURN nombre_programa;
END //

-- Función 6: buscar correo usuario
DELIMITER //
CREATE FUNCTION correo_usuario(doc INT)
RETURNS VARCHAR(30)
READS SQL DATA
BEGIN
    DECLARE correo VARCHAR(30);
    SELECT u.correo INTO correo FROM Usuario u WHERE u.documento = doc;
    RETURN correo;
END //
DELIMITER ;

-- Función 7: buscar coordinación de programa
DELIMITER //
CREATE FUNCTION buscar_coordinacion_aprendiz(id_aprendiz INT)
RETURNS VARCHAR(30)
READS SQL DATA
BEGIN
    DECLARE nombre_coord VARCHAR(30);
    SELECT c.nombre_coordinacion INTO nombre_coord
    FROM aprendiz a
    JOIN programas p ON a.programa_id = p.id_programas
    JOIN coordinacion c ON p.coordinacion_id = c.id_coordinacion
    WHERE a.id_aprendiz = id_aprendiz;
    RETURN nombre_coord;
END //
DELIMITER ;
