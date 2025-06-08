CREATE TABLE usuarios (
    nombre_usuario VARCHAR(50) PRIMARY KEY,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    privilegios TEXT NOT NULL
);

CREATE TABLE instructor (
  id_instructor INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  tipo_documento ENUM('CC', 'TI', 'CE', 'Pasaporte') NOT NULL,
  numero_documento VARCHAR(20) UNIQUE NOT NULL,
  email VARCHAR(100) NOT NULL,
  telefono VARCHAR(20),
  coordinacion VARCHAR(100) NOT NULL,
  fichas_asignadas TEXT NOT NULL,
  especialidad VARCHAR(100),
  fecha_ingreso DATE,
  estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo'
);