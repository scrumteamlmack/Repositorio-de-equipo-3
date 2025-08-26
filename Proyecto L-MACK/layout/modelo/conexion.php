<?php

class Conexion{
    
    private $host = "localhost";
    private $db = "mydb";
    private $user = "root";
    private $password = "";

    public static function getConexion(){
$conexion= null;
try{
   // $conexion = new PDO("mysql:host=".$host.";dbname=$db", $user, $password);
   $conexion = new PDO("mysql:host=localhost;dbname=mydb", "root", "");
    $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
//echo "Conexion exitosa a la base de datos.";
} catch(PDOException $e){
    echo "Error de conexion: ".$e->getMessage();
    }
    return $conexion;
  }
}
?>