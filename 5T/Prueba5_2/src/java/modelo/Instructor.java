package modelo;

import java.io.Serializable;

public class Instructor implements Serializable {
    
    private int idInstructor;
    private int idUsuario;
    private String email;
    private String telefono;
    private int coordinacionId;
    private String estado;
    
    public Instructor() {}
    
    public int getIdInstructor() {
        return idInstructor;
    }
    
    public void setIdInstructor(int idInstructor) {
        this.idInstructor = idInstructor;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public int getCoordinacionId() {
        return coordinacionId;
    }
    
    public void setCoordinacionId(int coordinacionId) {
        this.coordinacionId = coordinacionId;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
