<?php
require_once "../modelo/minutaDAO.php";

$minutaDAO = new minutaDAO();

if (isset($_GET['id'])) {
    $id = intval($_GET['id']);

    if ($minutaDAO->eliminarMinuta($id)) {
        header("Location: consultar_minutas.php");
        exit;
    } else {
        echo "Error al eliminar usuario.";
    }
} else {
    die("ID no proporcionado");
}
?>
