package modelo;

import java.io.Serializable;

public class TipoIncidente implements Serializable {

    private int idTipoIncidente;
    private String nombre;
    private String descripcion;

    public int getIdTipoIncidente() {
        return idTipoIncidente;
    }

    public void setIdTipoIncidente(int idTipoIncidente) {
        this.idTipoIncidente = idTipoIncidente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

