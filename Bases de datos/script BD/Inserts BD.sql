-- Insertar roles
INSERT INTO rol (nombre_rol) VALUES 
('Administrador'), ('Instructor'), ('Aprendiz');

-- Insertar usuarios
INSERT INTO Usuario (documento, nombre, apellido, tipo_documento, telefono, correo, contrasena) VALUES
(1010, 'Ana', 'López', 'CC', 3001234567, 'ana@mail.com', 'pass1'),
(2020, 'Carlos', 'Martínez', 'TI', 3012345678, 'carlos@mail.com', 'pass2'),
(3030, 'Lucía', 'Ramos', 'CC', 3123456789, 'lucia@mail.com', 'pass3');

-- Asociar roles
INSERT INTO user_rol (doc_id, rol_id) VALUES 
(1010, 1),
(2020, 2),
(3030, 3);

-- Insertar modalidades
INSERT INTO modalidad (nombre_modalidad) VALUES ('presencial'), ('sincronica');

-- Insertar jornadas
INSERT INTO jornada (nombre_jornada) VALUES ('mañana'), ('tarde'), ('noche'), ('madrugada');

-- Insertar coordinaciones
INSERT INTO coordinacion (nombre_coordinacion, correo_cordinacion) VALUES 
('Coordinación TIC', 'tic@mail.com'),
('Coordinación Académica', 'academica@mail.com');

-- Insertar programas
INSERT INTO programas (id_programas, nombre_programa, nivel_formacion, duracion, jornada_id, modalidad_id, coordinacion_id) VALUES
(1, 'ADSO', 'Tecnólogo', '24 meses', 1, 1, 1),
(2, 'Contabilidad', 'Técnico', '18 meses', 2, 2, 2),
(3, 'Amn empresa', 'tecnologo', '24 meses',1, 1, 1);

-- Insertar aprendices
INSERT INTO aprendiz (p_nombre, s_nombre, p_apellido, s_apellido, Num_ficha, programa_id, usua_id) VALUES
('Juan', 'Carlos', 'Gómez', 'Mora', 123456, 1, 3030),
('Mahilo', 'Yohan', 'Gutierrez', 'Ruiz', 3187969, 2, 2020),
('Luisa', 'Fernanda', 'Cruz', 'Montenegro', 3187969, 3, 3030);

-- Insertar ambientes
INSERT INTO ambiente (id_ambiente, num_ambiente, capacidad, tipo_ambiente, estado, coordinacion_id) VALUES
(1, 101, 30, 'Sala de Sistemas', 'Disponible', 1),
(2, 202, 20, 'Aula', 'Mantenimiento', 2);

-- insertar tipo recurso
INSERT INTo tipo_recurso(id_recurso, recurso_tipo, descripcion) VALUES 
(1, 'conputador', 'negro'),
(2, 'televisor', 'azul');

-- Insertar recurso
INSERT INTO recursos (serial_recurso, num_recurso, estado, observacion, ambiente_id, recurso_id) VALUES
('SR1001', 10, 'Operativo', 'ninguno', 1, 1),
('SR1002', 1, 'Dañado', 'dañado', 2, 2);

-- insertar tipo_recurso
INSERT INTO tipo_recurso (recurso_tipo, descripcion) VALUES
('teclado', 'inalámbrico'),
('mouse', 'óptico'),
('proyector', 'alta resolución');



-- Insertar registros de asistencia
INSERT INTO registro_asistencia (id_asistencia, fecha_asistencia, estado_asistencia, jorn_id, apr_id) VALUES
(1,'2025-06-01', 'N', 1, 1),
(2,'2025-06-02', 'E', 1, 2),
(3,'2025-06-03', 'S', 1, 3);

-- Insertar registros de minuta
INSERT INTO registro_minuta (fecha_hora_recibido, fecha_hora_entrega, novedad, responsable, descripcion_amb, ambiente_id, docu_id) VALUES
('2025-06-01 07:00:00', '2025-06-01 15:00:00', 'Todo normal', 'Carlos M.', 'Limpio y funcional', 1, 2020),
('2025-06-02 15:00:00', '2025-06-02 22:00:00', 'Revisión de ventilador', 'Ana L.', 'Ambiente cerrado', 2, 1010);

-- Insertar tipos de incidentes
INSERT INTO tipo_incidente (tipo_incidente, observacion_inc) VALUES
('Fallo Eléctrico', 'Se fue la luz en el aula 202'),
('Fuga de agua', 'Humedad en el techo del ambiente 101');

-- Insertar incidentes
INSERT INTO registro_incidente (descripcion, fecha_incidente, hora_incidente, ambiente_id, tipo_inc_id) VALUES
('Apagón durante la clase', '2025-06-01', '09:30:00', 2, 1),
('Filtración de agua en equipos', '2025-06-02', '11:00:00', 1, 2);
