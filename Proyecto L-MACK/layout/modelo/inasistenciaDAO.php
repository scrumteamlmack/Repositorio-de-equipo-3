<?php
require_once "conexion.php";
require_once "inasistenciaDTO.php";

class inasistenciaDAO {
    public function registrarInasistencia(inasistenciaDTO $inasistenciaDTO): bool {
        $conexion = Conexion::getConexion();
        $sql = "INSERT INTO registro_inasistencia 
        (fecha_inasistencia, estado_inasistencia, jornada_id, aprendiz_Usuario_id_usuario, instructor_Usuario_id_usuario)
        VALUES (?, ?, ?, ?, ?)";

        $fecha_inasistencia = $inasistenciaDTO->getFechaIna();
        $estado_Inasistencia = $inasistenciaDTO->getEstadoIna();
        $jornada_Id = $inasistenciaDTO->getJornadaId();
        $aprendiz_Id = $inasistenciaDTO->getAprendizId();
        $instructor_Id = $inasistenciaDTO->getInstructorId();

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindParam(1, $fecha_inasistencia); 
            $stmt->bindParam(2, $estado_Inasistencia); 
            $stmt->bindParam(3, $jornada_Id);    
            $stmt->bindParam(4, $aprendiz_Id);   
            $stmt->bindParam(5, $instructor_Id);

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al registrar inasistencia: " . $e->getMessage();
            return false; 
        }
    }

    public function listarInasistencia(): array {
        $conexion = Conexion::getConexion();
        $sql = "SELECT * FROM registro_inasistencia";
        try {
            $stmt = $conexion->prepare($sql);
            $stmt->execute();
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            echo "Error al consultar inasistencias: " . $e->getMessage();
            return [];
        }
    }

    public function actualizarInasistencia(inasistenciaDTO $inasistenciaDTO): bool {
        $conexion = Conexion::getConexion();
        $sql = "UPDATE registro_inasistencia 
                SET fecha_inasistencia=?, estado_inasistencia=?, jornada_id=?, aprendiz_Usuario_id_usuario=?, instructor_Usuario_id_usuario=?, ambiente_id=?, guarda_seguridad_Usuario_id_usuario=?, responsable_id=?
                WHERE id_minuta=?";

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindValue(1, $inasistenciaDTO->getFechaIna());
            $stmt->bindValue(2, $inasistenciaDTO->getEstadoIna());
            $stmt->bindValue(3, $inasistenciaDTO->getJornadaId());
            $stmt->bindValue(4, $inasistenciaDTO->getAprendizId());
            $stmt->bindValue(5, $inasistenciaDTO->getInstructorId());
            $stmt->bindValue(6, $inasistenciaDTO->getIdInasistencia()); 

            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al actualizar inasistencia: " . $e->getMessage();
            return false;
        }
    }

    public function eliminarInasistencia(int $id): bool {
        $conexion = Conexion::getConexion();
        $sql = "DELETE FROM registro_inasistencia WHERE id_inasistencia=?";

        try {
            $stmt = $conexion->prepare($sql);
            $stmt->bindValue(1, $id);
            return $stmt->execute();
        } catch (PDOException $e) {
            echo "Error al eliminar inasistencia: " . $e->getMessage();
            return false;
        }
    }
}
?>
