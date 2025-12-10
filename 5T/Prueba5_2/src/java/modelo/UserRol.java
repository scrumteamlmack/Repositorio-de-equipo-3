package modelo;

import java.io.Serializable;

public class UserRol implements Serializable {

    private int idUserRol;
    private int idUsuario;
    private int idRol;

    public int getIdUserRol() {
        return idUserRol;
    }

    public void setIdUserRol(int idUserRol) {
        this.idUserRol = idUserRol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
}

