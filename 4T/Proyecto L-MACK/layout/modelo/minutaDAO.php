<?php
require_once "conexion.php";
require_once "minutaDTO.php";

class minutaDAO {
    public function registrarMinuta(minutaDTO $minutaDTO): bool {
        $conexion = Conexion::getConexion();
        $sql = "INSERT INTO registro_minuta 
        (fecha_hora_recibo, fecha_hora_entrega, novedad, descripcion_min, estado, ambiente_id, guarda_seguridad_Usuario_id_usuario, responsable_id)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        $fecha_hora_recibo = $minutaDTO->getFechaRec();
        $fecha_hora_entrega = $minutaDTO->getFechaEnt();
        $novedad = $minutaDTO->getNovedad();
        $descripcion_min = $minutaDTO->getDescripcion();
        $estado = $minutaDTO->getEstado();
        $ambiente_id = $minutaDTO->getAmbiente();
        $guarda_seguridad_Usuario_id_usuario = $minutaDTO->getGuarda();
        $responsable_id = $minutaDTO->getResponsable();

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindParam(1, $fecha_hora_recibo); 
            $stmt->bindParam(2, $fecha_hora_entrega);    
            $stmt->bindParam(3, $novedad);  
            $stmt->bindParam(4, $descripcion_min);    
            $stmt->bindParam(5, $estado);   
            $stmt->bindParam(6, $ambiente_id);     
            $stmt->bindParam(7, $guarda_seguridad_Usuario_id_usuario);     
            $stmt->bindParam(8, $responsable_id); 

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al registrar minuta: " . $e->getMessage();
            return false; 
        }
    }

    public function listarMinuta(): array {
        $conexion = Conexion::getConexion();
        $sql = "SELECT * FROM registro_minuta";
        try {
            $stmt = $conexion->prepare($sql);
            $stmt->execute();
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            echo "Error al consultar minutas: " . $e->getMessage();
            return [];
        }
    }

    public function actualizarMinuta(minutaDTO $minutaDTO): bool {
        $conexion = Conexion::getConexion();
        $sql = "UPDATE registro_minuta 
                SET fecha_hora_recibo=?, fecha_hora_entrega=?, novedad=?, descripcion_min=?, estado=?, ambiente_id=?, guarda_seguridad_Usuario_id_usuario=?, responsable_id=?
                WHERE id_minuta=?";

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindValue(1, $minutaDTO->getFechaRec());
            $stmt->bindValue(2, $minutaDTO->getFechaEnt());
            $stmt->bindValue(3, $minutaDTO->getNovedad());
            $stmt->bindValue(4, $minutaDTO->getDescripcion());
            $stmt->bindValue(5, $minutaDTO->getEstado());
            $stmt->bindValue(6, $minutaDTO->getAmbiente());
            $stmt->bindValue(7, $minutaDTO->getGuarda());
            $stmt->bindValue(8, $minutaDTO->getResponsable());
            $stmt->bindValue(9, $minutaDTO->getIdMinuta()); 

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al actualizar minuta: " . $e->getMessage();
            return false;
        }
    }

    public function eliminarMinuta(int $id): bool {
        $conexion = Conexion::getConexion();
        $sql = "DELETE FROM registro_minuta WHERE id_minuta=?";

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindValue(1, $id);
            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al eliminar minuta: " . $e->getMessage();
            return false;
        }
    }
}
?>
