<?php

class Conexion{
    
    private $host = "localhost";
    private $db = "mydb";
    private $user = "root";
    private $password;

    public function __construct(){
        // Obtener la contraseña desde la variable de entorno
        $this->password = getenv("MYSQL_SECURE_PASSWORD");
    }

    public static function getConexion(){
$conexion= null;
try{
   // Crear instancia de la clase para acceder a las propiedades
            $instancia = new self();
   // $conexion = new PDO("mysql:host=".$host.";dbname=$db", $user, $password);
   $conexion = new PDO("mysql:host=" . $instancia->host . ";dbname=" . $instancia->db,
                $instancia->user,
                $instancia->password);
    $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
//echo "Conexion exitosa a la base de datos.";
} catch(PDOException $e){
    echo "Error de conexion: ".$e->getMessage();
    }
    return $conexion;
  }
}
?>