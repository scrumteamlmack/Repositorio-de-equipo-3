CREATE TABLE usuarios (
    nombre_usuario VARCHAR(50) PRIMARY KEY,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    privilegios TEXT NOT NULL
);
