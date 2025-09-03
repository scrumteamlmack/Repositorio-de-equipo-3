<?php
require_once "../modelo/recursosDAO.php";

$recursosDAO = new recursosDAO();

if (isset($_GET['id'])) {
    $id = intval($_GET['id']);

    if ($recursosDAO->eliminarRecursos($id)) {
        header("Location: consultar_recursos.php");
        exit;
    } else {
        echo "Error al eliminar usuario.";
    }
} else {
    die("ID no proporcionado");
}
?>
