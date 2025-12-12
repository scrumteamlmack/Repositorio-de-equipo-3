<?php
require_once "../modelo/inasistenciaDAO.php";

$inasistenciaDAO = new inasistenciaDAO();

if (isset($_GET['id'])) {
    $id = intval($_GET['id']);

    if ($inasistenciaDAO->eliminarInasistencia($id)) {
        header("Location: consultar_inasistencia.php");
        exit;
    } else {
        echo "Error al eliminar inasistencia.";
    }
} else {
    die("ID no proporcionado");
}
?>
