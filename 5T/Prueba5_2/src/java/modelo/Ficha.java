package modelo;

import java.io.Serializable;

public class Ficha implements Serializable {

    private int idFicha;
    private int numFicha;
<<<<<<< HEAD
    private int instructorUsuarioId; 

 
=======
    private int instructorUsuarioId; // instructor_Usuario_id_usuario

    // Getters y Setters
>>>>>>> ac35112eaecad7a929d85524ba6402890ab0acaf
    public int getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(int idFicha) {
        this.idFicha = idFicha;
    }

    public int getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(int numFicha) {
        this.numFicha = numFicha;
    }

    public int getInstructorUsuarioId() {
        return instructorUsuarioId;
    }

    public void setInstructorUsuarioId(int instructorUsuarioId) {
        this.instructorUsuarioId = instructorUsuarioId;
    }
}
