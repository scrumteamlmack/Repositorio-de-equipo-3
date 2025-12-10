package modelo;

import java.io.Serializable;
import java.util.Date;

public class GuardaSeguridad implements Serializable {
    
    private int idGuardaSeguridad;
    private int idUsuario;
    private String turno;
    private Date fechaIngreso;
    private String estado;
    
    public GuardaSeguridad() {}
    
    public int getIdGuardaSeguridad() {
        return idGuardaSeguridad;
    }
    
    public void setIdGuardaSeguridad(int idGuardaSeguridad) {
        this.idGuardaSeguridad = idGuardaSeguridad;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getTurno() {
        return turno;
    }
    
    public void setTurno(String turno) {
        this.turno = turno;
    }
    
    public Date getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
