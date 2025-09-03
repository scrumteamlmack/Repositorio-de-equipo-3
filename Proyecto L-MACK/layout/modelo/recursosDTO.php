<?php 
class recursosDTO{
    private $id_recurso;
    private $serial_recurso;
    private $num_recurso;
    private $nombre_recurso;
    private $tipo_recurso;
private $estadoRec;
    private $observacion;
    private $ambiente_id_rec;



    public function getIdRecurso() {
        return $this->id_recurso;
    }

    public function getSerialRec() {
        return $this->serial_recurso;
    }

    public function getNumRec() {
        return $this->num_recurso;
    }

    public function getNomRec() {
        return $this->nombre_recurso;
    }

    public function getTipoRec() {
        return $this->tipo_recurso;
    }

    public function getEstadoRec() {
        return $this->estadoRec;
    }

    public function getObservacion() {
        return $this->observacion;
    }
    public function getAmbienteRec() {
        return $this->ambiente_id_rec;
    }



    public function setIdRecurso($id_recurso) {
        $this->id_recurso = $id_recurso;
    }

    public function setSerialRec($serial_recurso) {
        $this->serial_recurso = $serial_recurso;
    }

    public function setNumRec($num_recurso) {
        $this->num_recurso = $num_recurso;
    }

    public function setNomRec($nombre_recurso) {
        $this->nombre_recurso = $nombre_recurso;
    }

    public function setTipoRecurso($tipo_recurso) {
        $this->tipo_recurso = $tipo_recurso;
    }

    public function setEstadoRec($estadoRec) {
        $this->estadoRec = $estadoRec;
    }
    
    public function setObservacion($observacion) {
        $this->observacion = $observacion;
    }

     public function setAmbienteRec($ambiente_id_rec) {
        $this->ambiente_id_rec = $ambiente_id_rec;
    }


    
}
?> 