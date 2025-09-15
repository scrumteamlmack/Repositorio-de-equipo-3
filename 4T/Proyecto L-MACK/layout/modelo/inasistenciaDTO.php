<?php 
class inasistenciaDTO{
    private $id_inasistencia;
    private $fecha_inasistencia;
    private $estado_inasistencia;
    private $jornada_id;
    private $aprendiz_id;
    private $instructor_id;



    public function getIdInasistencia() {
        return $this->id_inasistencia;
    }

    public function getFechaIna() {
        return $this->fecha_inasistencia;
    }

    public function getEstadoIna() {
        return $this->estado_inasistencia;
    }

    public function getJornadaId() {
        return $this->jornada_id;
    }

    public function getAprendizId() {
        return $this->aprendiz_id;
    }

    public function getInstructorId() {
        return $this->instructor_id;
    }



    public function setIdInasistencia($id_inasistencia) {
        $this->id_inasistencia = $id_inasistencia;
    }

    public function setFechaIna($fecha_inasistencia) {
        $this->fecha_inasistencia = $fecha_inasistencia;
    }

    public function setEstadoIna($estado_inasistencia) {
        $this->estado_inasistencia = $estado_inasistencia;
    }

    public function setJornada($jornada_id) {
        $this->jornada_id = $jornada_id;
    }

    public function setAprendiz($aprendiz_id) {
        $this->aprendiz_id = $aprendiz_id;
    }

    public function setInstructor($instructor_id) {
        $this->instructor_id = $instructor_id;
    }
    
    
}
?> 