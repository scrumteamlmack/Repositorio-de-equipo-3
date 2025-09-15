<?php
require_once '../modelo/conexion.php';
require_once '../modelo/minutaDAO.php';
require_once '../modelo/minutaDTO.php';

if(isset($_POST['btnMinuta'])){
    
    $fecha_hora_recibo = $_POST['fecha_hora_recibo'];
    $fecha_hora_entrega = $_POST['fecha_hora_entrega'];
    $novedad = $_POST['novedad'];
    $descripcion_min = $_POST['descripcion_min'];
    $estado = $_POST['estado'];
    $ambiente_id = $_POST['ambiente_id'];
    $guarda_seguridad_Usuario_id_usuario = $_POST['guarda_seguridad_Usuario_id_usuario'];
    $responsable_id = $_POST['responsable_id'];
    


    $minutaDTO = new minutaDTO() ;
    $minutaDTO->setFechaRec($fecha_hora_recibo);
    $minutaDTO->setFechaEnt($fecha_hora_entrega);
    $minutaDTO->setNovedad($novedad);
    $minutaDTO->setDescripcion($descripcion_min);
    $minutaDTO->setEstado($estado);
    $minutaDTO->setAmbiente($ambiente_id);
    $minutaDTO->setGuarda($guarda_seguridad_Usuario_id_usuario);
    $minutaDTO->setResponsable($responsable_id);
    

    $minutaDAO = new minutaDAO() ;
    $mensaje = $minutaDAO->registrarMinuta($minutaDTO);
    echo "<script>window.location.replace('../vista/consultar_minutas.php');</script>";



    



    
}
?>