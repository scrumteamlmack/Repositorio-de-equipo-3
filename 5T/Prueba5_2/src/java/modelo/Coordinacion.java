package modelo;

import java.io.Serializable;

public class Coordinacion implements Serializable {
    
    private int idCoordinacion;
    private String nombreCoordinacion;
    private String correoCoordinacion;
    
    public Coordinacion() {}
    
    public int getIdCoordinacion() {
        return idCoordinacion;
    }
    
    public void setIdCoordinacion(int idCoordinacion) {
        this.idCoordinacion = idCoordinacion;
    }
    
    public String getNombreCoordinacion() {
        return nombreCoordinacion;
    }
    
    public void setNombreCoordinacion(String nombreCoordinacion) {
        this.nombreCoordinacion = nombreCoordinacion;
    }
    
    public String getCorreoCoordinacion() {
        return correoCoordinacion;
    }
    
    public void setCorreoCoordinacion(String correoCoordinacion) {
        this.correoCoordinacion = correoCoordinacion;
    }
}

