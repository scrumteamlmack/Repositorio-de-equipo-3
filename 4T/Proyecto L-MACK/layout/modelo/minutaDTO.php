<?php 
class minutaDTO{
    private $id_minuta;
    private $fecha_hora_recibo;
    private $fecha_hora_entrega;
    private $novedad;
    private $descripcion_min;
    private $estado;
    private $ambiente_id;
    private $guarda_seguridad_Usuario_id_usuario;
    private $responsable_id;



    public function getIdMinuta() {
        return $this->id_minuta;
    }

    public function getFechaRec() {
        return $this->fecha_hora_recibo;
    }

    public function getFechaEnt() {
        return $this->fecha_hora_entrega;
    }

    public function getNovedad() {
        return $this->novedad;
    }

    public function getDescripcion() {
        return $this->descripcion_min;
    }

    public function getEstado() {
        return $this->estado;
    }

    public function getAmbiente() {
        return $this->ambiente_id;
    }
    public function getGuarda() {
        return $this->guarda_seguridad_Usuario_id_usuario;
    }
    public function getResponsable() {
        return $this->responsable_id;
    }


    public function setIdMinuta($id_minuta) {
        $this->id_minuta = $id_minuta;
    }

    public function setFechaRec($fecha_hora_recibo) {
        $this->fecha_hora_recibo = $fecha_hora_recibo;
    }

    public function setFechaEnt($fecha_hora_entrega) {
        $this->fecha_hora_entrega = $fecha_hora_entrega;
    }

    public function setNovedad($novedad) {
        $this->novedad = $novedad;
    }

    public function setDescripcion($descripcion_min) {
        $this->descripcion_min = $descripcion_min;
    }

    public function setEstado($estado) {
        $this->estado = $estado;
    }
    
    public function setAmbiente($ambiente_id) {
        $this->ambiente_id = $ambiente_id;
    }

     public function setGuarda($guarda_seguridad_Usuario_id_usuario) {
        $this->guarda_seguridad_Usuario_id_usuario = $guarda_seguridad_Usuario_id_usuario;
    }

     public function setResponsable($responsable_id) {
        $this->responsable_id = $responsable_id;
    }
    
}
?> 