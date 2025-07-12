package org.CMVD.Softwork.DTO.Usuario;

public class UsuarioDTO {
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String apellidoP;
    private String apellidoM;
    private String claveCifDesPersonal;
    private String contrasena;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Integer idUsuario, String nombre, String correo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getClaveCifDesPersonal() {
        return claveCifDesPersonal;
    }

    public void setClaveCifDesPersonal(String claveCifDesPersonal) {
        this.claveCifDesPersonal = claveCifDesPersonal;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public UsuarioDTO(Integer idUsuario, String nombre, String correo, String apellidoP, String apellidoM, String claveCifDesPersonal, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.claveCifDesPersonal = claveCifDesPersonal;
        this.contrasena = contrasena;
    }

}