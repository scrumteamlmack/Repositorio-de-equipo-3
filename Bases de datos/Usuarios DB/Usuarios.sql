use mydb;

-- 1. LUIS VARGAS - Solo Administrador (ID: 5)
DROP USER IF EXISTS 'luis.vargas'@'localhost';
CREATE USER 'luis.vargas'@'localhost' IDENTIFIED BY 'KennerStorm#SC';
GRANT ALL PRIVILEGES ON mydb.* TO 'luis.vargas'@'localhost' WITH GRANT OPTION;

--  2. CARLOS GARCÍA - Coordinador (ID: 1)
DROP USER IF EXISTS 'carlos.garcia'@'localhost';
CREATE USER 'carlos.garcia'@'localhost' IDENTIFIED BY 'C@rlosC0ord#25';
GRANT SELECT, INSERT, UPDATE ON mydb.aprendiz TO 'carlos.garcia'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.registro_asistencia TO 'carlos.garcia'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.programas TO 'carlos.garcia'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.registro_incidente TO 'carlos.garcia'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.ambiente TO 'carlos.garcia'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.recursos TO 'carlos.garcia'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.jornada TO 'carlos.garcia'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.modalidad TO 'carlos.garcia'@'localhost';

--  3. GLORIA PARRA - Aprendiz (ID: 16)
DROP USER IF EXISTS 'gloria.parra'@'localhost';
CREATE USER 'gloria.parra'@'localhost' IDENTIFIED BY 'M@hilyK2025!';
GRANT SELECT, UPDATE ON mydb.aprendiz TO 'gloria.parra'@'localhost';
GRANT SELECT ON mydb.registro_asistencia TO 'gloria.parra'@'localhost';
GRANT SELECT ON mydb.programas TO 'gloria.parra'@'localhost';

--  4. ROSA ARANGO - Guarda de Seguridad (ID: 28)
DROP USER IF EXISTS 'rosa.arango'@'localhost';
CREATE USER 'rosa.arango'@'localhost' IDENTIFIED BY 'D4v1dSC#08';
GRANT SELECT, UPDATE ON mydb.registro_minuta TO 'rosa.arango'@'localhost';
GRANT SELECT, UPDATE ON mydb.registro_incidente TO 'rosa.arango'@'localhost';
GRANT SELECT, UPDATE ON mydb.ambiente TO 'rosa.arango'@'localhost';
GRANT SELECT ON mydb.recursos TO 'rosa.arango'@'localhost';

--  5. ANDRÉS ROJAS - Instructor (ID: 13)
DROP USER IF EXISTS 'andres.rojas'@'localhost';
CREATE USER 'andres.rojas'@'localhost' IDENTIFIED BY 'E.CamiloA$92';
GRANT SELECT ON mydb.aprendiz TO 'andres.rojas'@'localhost';
GRANT SELECT ON mydb.ambiente TO 'andres.rojas'@'localhost';
GRANT SELECT ON mydb.registro_minuta TO 'andres.rojas'@'localhost';
GRANT SELECT, UPDATE ON mydb.registro_incidente TO 'andres.rojas'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.registro_asistencia TO 'andres.rojas'@'localhost';
GRANT SELECT ON mydb.recursos TO 'andres.rojas'@'localhost';

FLUSH PRIVILEGES;
