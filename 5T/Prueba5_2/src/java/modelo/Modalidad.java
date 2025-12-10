package modelo;

import java.io.Serializable;

public class Modalidad implements Serializable {
    
    private int idModalidad;
    private String nombreModalidad;
    
    public Modalidad() {}
    
    public int getIdModalidad() {
        return idModalidad;
    }
    
    public void setIdModalidad(int idModalidad) {
        this.idModalidad = idModalidad;
    }
    
    public String getNombreModalidad() {
        return nombreModalidad;
    }
    
    public void setNombreModalidad(String nombreModalidad) {
        this.nombreModalidad = nombreModalidad;
    }
}

