<?php
require_once "conexion.php";
require_once "recursosDTO.php";

class recursosDAO {
    public function registrarRecursos(recursosDTO $recursosDTO): bool {
        $conexion = Conexion::getConexion();
        $sql = "INSERT INTO recursos 
        (serial_recurso, num_recurso, nombre_recurso, tipo_recurso, estado, observacion, ambiente_id)
        VALUES (?, ?, ?, ?, ?, ?, ?)";

        $serialRecurso = $recursosDTO->getSerialRec();
        $Num_recurso = $recursosDTO->getNumRec();
        $Nom_recurso = $recursosDTO->getNomRec();
        $Tipo_recurso = $recursosDTO->getTipoRec();
        $Estado_recurso = $recursosDTO->getEstadoRec();
        $Observacion_recurso = $recursosDTO->getObservacion();
        $Ambiente_recurso = $recursosDTO->getAmbienteRec();
        

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindParam(1, $serialRecurso); 
            $stmt->bindParam(2, $Num_recurso);    
            $stmt->bindParam(3, $Nom_recurso);  
            $stmt->bindParam(4, $Tipo_recurso);    
            $stmt->bindParam(5, $Estado_recurso);   
            $stmt->bindParam(6, $Observacion_recurso);     
            $stmt->bindParam(7, $Ambiente_recurso);      

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al registrar el recurso: " . $e->getMessage();
            return false; 
        }
    }

    public function listarRecursos(): array {
        $conexion = Conexion::getConexion();
        $sql = "SELECT * FROM recursos";
        try {
            $stmt = $conexion->prepare($sql);
            $stmt->execute();
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            echo "Error al consultar recursos: " . $e->getMessage();
            return [];
        }
    }

    public function actualizarRecursos(recursosDTO $recursosDTO): bool {
        $conexion = Conexion::getConexion();
        $sql = "UPDATE recursos 
                SET serial_recurso=?, num_recurso=?, nombre_recurso=?, tipo_recurso=?, estado=?, observacion=?, ambiente_id=?
                WHERE id_recurso=?";

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindValue(1, $recursosDTO->getSerialRec());
            $stmt->bindValue(2, $recursosDTO->getNumRec());
            $stmt->bindValue(3, $recursosDTO->getNomRec());
            $stmt->bindValue(4, $recursosDTO->getTipoRec());
            $stmt->bindValue(5, $recursosDTO->getEstadoRec());
            $stmt->bindValue(6, $recursosDTO->getObservacion());
            $stmt->bindValue(7, $recursosDTO->getAmbienteRec());
            $stmt->bindValue(8, $recursosDTO->getIdRecurso()); 

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al actualizar recurso: " . $e->getMessage();
            return false;
        }
    }

    public function eliminarRecursos(int $id): bool {
        $conexion = Conexion::getConexion();
        $sql = "DELETE FROM recursos WHERE id_recurso=?";

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindValue(1, $id);
            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al eliminar recurso: " . $e->getMessage();
            return false;
        }
    }
}
?>
