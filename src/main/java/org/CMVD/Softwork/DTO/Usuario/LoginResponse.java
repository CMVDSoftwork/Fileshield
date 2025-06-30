package org.CMVD.Softwork.DTO.Usuario;

public class LoginResponse {
    private String token;
    private String tipoToken= "Bearer";
    private String correo;
    private String nombre;
    private Integer idUsuario;
    private String claveCifDesPersonal;

    public LoginResponse() {
    }

    public LoginResponse(String token, String tipoToken, String correo, String nombre, Integer idUsuario, String claveCifDesPersonal) {
        this.token = token;
        this.tipoToken = tipoToken;
        this.correo = correo;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
        this.claveCifDesPersonal = claveCifDesPersonal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getClaveCifDesPersonal() {
        return claveCifDesPersonal;
    }

    public void setClaveCifDesPersonal(String claveCifDesPersonal) {
        this.claveCifDesPersonal = claveCifDesPersonal;
    }
}
