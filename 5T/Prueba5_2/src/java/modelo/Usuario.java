package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
public class Usuario implements Serializable {
    private int idUsuario;
    private String pNombre;
    private String sNombre;
    private String pApellido;
    private String sApellido;
    private String tipoDocumento;
    private int numDocumento;
    private String correo;
    private String contrasena;
    private int Telefono;
    private String Activo;
  
    private String pass1;

    public int getTelefono() {
        return Telefono;
    }

    public void setTelefono(int Telefono) {
        this.Telefono = Telefono;
    }

    public String getActivo() {
        return Activo;
    }

    public void setActivo(String Activo) {
        this.Activo = Activo;
    }
    

    public Usuario() {}


    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getPNombre() { return pNombre; }
    public void setPNombre(String pNombre) { this.pNombre = pNombre; }

    public String getSNombre() { return sNombre; }
    public void setSNombre(String sNombre) { this.sNombre = sNombre; }

    public String getPApellido() { return pApellido; }
    public void setPApellido(String pApellido) { this.pApellido = pApellido; }

    public String getSApellido() { return sApellido; }
    public void setSApellido(String sApellido) { this.sApellido = sApellido; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public int getNumDocumento() { return numDocumento; }
    public void setNumDocumento(int numDocumento) { this.numDocumento = numDocumento; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getPass1() { return pass1; }
    public void setPass1(String pass1) { this.pass1 = pass1; }


    public int getDoc() { return this.numDocumento; }
    public void setDoc(int doc) { this.numDocumento = doc; }


    public String getNombre() {
        String a = (pNombre != null ? pNombre : "");
        String b = (pApellido != null ? pApellido : "");
        return (a + " " + b).trim();
    }


    public String getPass() { return this.contrasena; }
    public void setPass(String pass) { this.contrasena = pass; }

    public void setFechaRegistro(LocalDateTime toLocalDateTime) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
