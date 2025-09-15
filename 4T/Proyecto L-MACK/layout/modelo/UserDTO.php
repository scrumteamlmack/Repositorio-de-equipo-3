<?php 
class UserDTO{
    private $id;
    private $p_nombre;
    private $s_nombre;
    private $p_apellido;
    private $s_apellido;
    private $documento;
    private $tipo_documento;
    private $correo;
    private $clave;


    public function getId() {
        return $this->id;
    }

    public function getDocumento() {
        return $this->documento;
    }

    public function getPNombre() {
        return $this->p_nombre;
    }

    public function getSNombre() {
        return $this->s_nombre;
    }

    public function getPApellido() {
        return $this->p_apellido;
    }

    public function getSApellido() {
        return $this->s_apellido;
    }

    public function getTipoDocumento() {
        return $this->tipo_documento;
    }
    public function getCorreo() {
        return $this->correo;
    }
    public function getClave() {
        return $this->clave;
    }

    public function setId($id) {
        $this->id = $id;
    }

    public function setDocumento($documento) {
        $this->documento = $documento;
    }

    public function setP_Nombre($p_nombre) {
        $this->p_nombre = $p_nombre;
    }

    public function setS_Nombre($s_nombre) {
        $this->s_nombre = $s_nombre;
    }

    public function setP_Apellido($p_apellido) {
        $this->p_apellido = $p_apellido;
    }

    public function setS_Apellido($s_apellido) {
        $this->s_apellido = $s_apellido;
    }
    
    public function setTipoDocumento($tipo_documento) {
        $this->tipo_documento = $tipo_documento;
    }

     public function setCorreo($correo) {
        $this->correo = $correo;
    }

     public function setClave($clave) {
        $this->clave = $clave;
    }
    
}
?> 