<?php
require_once "conexion.php";
require_once "UserDTO.php";

class UserDAO {
    public function registrarPersona(UserDTO $UserDTO): bool {
        $conexion = Conexion::getConexion();
        $sql = "INSERT INTO usuario 
                (p_nombre, s_nombre, p_apellido, s_apellido, num_documento, tipo_documento, correo, contraseña) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        $p_nombre = $UserDTO->getPNombre();
        $s_nombre = $UserDTO->getSNombre();
        $p_apellido = $UserDTO->getPApellido();
        $s_apellido = $UserDTO->getSApellido();
        $documento = $UserDTO->getDocumento();
        $tipo_documento = $UserDTO->getTipoDocumento();
        $correo = $UserDTO->getCorreo();
        $clave = $UserDTO->getClave();

     
        $claveHash = password_hash($clave, PASSWORD_DEFAULT);

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindParam(1, $p_nombre); 
            $stmt->bindParam(2, $s_nombre);    
            $stmt->bindParam(3, $p_apellido);  
            $stmt->bindParam(4, $s_apellido);    
            $stmt->bindParam(5, $documento);   
            $stmt->bindParam(6, $tipo_documento);     
            $stmt->bindParam(7, $correo);     
            $stmt->bindParam(8, $claveHash);   // 👈 Guardamos el hash, no la clave normal

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al registrar persona: " . $e->getMessage();
            return false; 
        }
    }

     // 🔎 Nuevo método para consultar los usuarios
    public function listarUsuarios(): array {
        $conexion = Conexion::getConexion();
        $sql = "SELECT id_usuario, p_nombre, s_nombre, p_apellido, s_apellido, num_documento, tipo_documento, correo 
                FROM usuario";
        try {
            $stmt = $conexion->prepare($sql);
            $stmt->execute();
            $usuarios = $stmt->fetchAll(PDO::FETCH_ASSOC);
            return $usuarios;
        } catch (PDOException $e) {
            echo "Error al consultar usuarios: " . $e->getMessage();
            return [];
        }
    }
    public function actualizarUsuario(UserDTO $UserDTO): bool {
        $conexion = Conexion::getConexion();
        $sql = "UPDATE usuario 
                SET p_nombre=?, s_nombre=?, p_apellido=?, s_apellido=?, num_documento=?, tipo_documento=?, correo=? 
                WHERE id_usuario=?";

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindValue(1, $UserDTO->getPNombre());
            $stmt->bindValue(2, $UserDTO->getSNombre());
            $stmt->bindValue(3, $UserDTO->getPApellido());
            $stmt->bindValue(4, $UserDTO->getSApellido());
            $stmt->bindValue(5, $UserDTO->getDocumento());
            $stmt->bindValue(6, $UserDTO->getTipoDocumento());
            $stmt->bindValue(7, $UserDTO->getCorreo());
            $stmt->bindValue(8, $UserDTO->getId()); // Necesita tener ID en el DTO

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al actualizar usuario: " . $e->getMessage();
            return false;
        }
    }

    // 🔹 Eliminar
    public function eliminarUsuario(int $id): bool {
        $conexion = Conexion::getConexion();
        $sql = "DELETE FROM usuario WHERE id_usuario=?";

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindValue(1, $id);
            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al eliminar usuario: " . $e->getMessage();
            return false;
        }
    }
}
?>
