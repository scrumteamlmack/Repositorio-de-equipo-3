<?php
require_once '../modelo/conexion.php';
require_once '../modelo/inasistenciaDAO.php';
require_once '../modelo/inasistenciaDTO.php';

if(isset($_POST['btnInasistencia'])){
    
    $fecha_inasistencia = $_POST['fecha_inasistencia'];
    $estado_inasistencia = $_POST['estado_inasistencia'];
    $jornada_id = $_POST['jornada_id'];
    $aprendiz_id = $_POST['aprendiz_id'];
    $instructor_id = $_POST['instructor_id'];
    


    $inasistenciaDTO = new inasistenciaDTO() ;
    $inasistenciaDTO->setFechaIna($fecha_inasistencia);
    $inasistenciaDTO->setEstadoIna($estado_inasistencia);
    $inasistenciaDTO->setJornada($jornada_id);
    $inasistenciaDTO->setAprendiz($aprendiz_id);
    $inasistenciaDTO->setInstructor($instructor_id);

    $inasistenciaDAO = new inasistenciaDAO() ;
    $mensaje = $inasistenciaDAO->registrarInasistencia($inasistenciaDTO);
    echo "<script>window.location.replace('../vista/consultar_inasistencia.php');</script>";



    



    
}
?>