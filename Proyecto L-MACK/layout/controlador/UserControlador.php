<?php
require_once '../modelo/conexion.php';
require_once '../modelo/UserDAO.php';
require_once '../modelo/UserDTO.php';

if(isset($_POST['btnRegistrar'])){
    
    $p_nombre = $_POST['p_nombre'];
    $s_nombre = $_POST['s_nombre'];
    $p_apellido = $_POST['p_apellido'];
    $s_apellido = $_POST['s_apellido'];
    $documento = $_POST['num_documento'];
    $tipo_documento = $_POST['tipo_documento'];
    $correo = $_POST['correo'];
    $clave = $_POST['contrasena'];
    


    $UserDTO = new UserDTO() ;
    $UserDTO->setP_Nombre($p_nombre);
    $UserDTO->setS_Nombre($s_nombre);
    $UserDTO->setP_Apellido($p_apellido);
    $UserDTO->setS_Apellido($s_apellido);
    $UserDTO->setDocumento($documento);
    $UserDTO->setTipoDocumento($tipo_documento);
    $UserDTO->setCorreo($correo);
    $UserDTO->setClave($clave);
    

    $UserDAO = new UserDAO() ;
    $mensaje = $UserDAO->registrarPersona($UserDTO);
    echo "<script>window.location.replace('../vista/consultar_usuarios.php');</script>";


// pasas la variable a la vista
include '../vista/html/consultar_minutas.php';


    



    
}
?>