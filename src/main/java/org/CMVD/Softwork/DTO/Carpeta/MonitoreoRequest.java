package org.CMVD.Softwork.DTO.Carpeta;

public class MonitoreoRequest {
    private Integer idUsuario;
    private String contrasena;
    private String ruta;

    public MonitoreoRequest() {
    }

    public MonitoreoRequest(Integer idUsuario, String contrasena, String ruta) {
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
        this.ruta = ruta;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
