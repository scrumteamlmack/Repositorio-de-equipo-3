package modelo;

import java.io.Serializable;

public class Jornada implements Serializable {
    
    private int idJornada;
    private String nombreJornada;
    
    public Jornada() {}
    
    public int getIdJornada() {
        return idJornada;
    }
    
    public void setIdJornada(int idJornada) {
        this.idJornada = idJornada;
    }
    
    public String getNombreJornada() {
        return nombreJornada;
    }
    
    public void setNombreJornada(String nombreJornada) {
        this.nombreJornada = nombreJornada;
    }
}

