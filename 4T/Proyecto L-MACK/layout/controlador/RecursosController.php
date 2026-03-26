<?php
require_once '../modelo/conexion.php';
require_once '../modelo/recursosDAO.php';
require_once '../modelo/recursosDTO.php';

if(isset($_POST['btnRecurso'])){
    
    $serialRecurso = $_POST['serial_recurso'];
    $Num_recurso = $_POST['num_recurso'];
    $Nom_recurso = $_POST['nombre_recurso'];
    $Tipo_recurso = $_POST['tipo_recurso'];
    $Estado_recurso = $_POST['estadoRec'];
    $Observacion_recurso = $_POST['observacion'];
    $Ambiente_recurso = $_POST['ambiente_id_rec'];
    


    $recursosDTO = new recursosDTO() ;
    $recursosDTO->setSerialRec($serialRecurso);
    $recursosDTO->setNumRec($Num_recurso);
    $recursosDTO->setNomRec($Nom_recurso);
    $recursosDTO->setTipoRecurso($Tipo_recurso);
    $recursosDTO->setEstadoRec($Estado_recurso);
    $recursosDTO->setObservacion($Observacion_recurso);
    $recursosDTO->setAmbienteRec($Ambiente_recurso);
    
    

    $recursosDAO = new recursosDAO() ;
    $mensaje = $recursosDAO->registrarRecursos($recursosDTO);
    echo "<script>window.location.replace('../vista/consultar_recursos.php');</script>";



    



    
}
?>