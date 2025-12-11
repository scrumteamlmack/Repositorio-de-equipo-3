package modelo;

import java.io.Serializable;

public class Coordinador implements Serializable {
    
    private int idCoordinador;
    private int idUsuario;
    private int coordinacionId;
    
    public Coordinador() {}
    
    public int getIdCoordinador() {
        return idCoordinador;
    }
    
    public void setIdCoordinador(int idCoordinador) {
        this.idCoordinador = idCoordinador;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public int getCoordinacionId() {
        return coordinacionId;
    }
    
    public void setCoordinacionId(int coordinacionId) {
        this.coordinacionId = coordinacionId;
    }
}
