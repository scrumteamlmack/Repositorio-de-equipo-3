<?php
require_once "../modelo/UserDAO.php";

$userDAO = new UserDAO();

if (isset($_GET['id'])) {
    $id = intval($_GET['id']);

    if ($userDAO->eliminarUsuario($id)) {
        header("Location: consultar_usuarios.php");
        exit;
    } else {
        echo "Error al eliminar usuario.";
    }
} else {
    die("ID no proporcionado");
}
?>
