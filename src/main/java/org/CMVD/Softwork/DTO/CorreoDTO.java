package org.CMVD.Softwork.DTO;

public class CorreoDTO {
    private Integer idCorreo;
    private String contenidoCifrado;
    private String claveCifDes;
    private String estatus;

    public CorreoDTO() {
    }

    public CorreoDTO(Integer idCorreo, String contenidoCifrado, String claveCifDes, String estatus) {
        this.idCorreo = idCorreo;
        this.contenidoCifrado = contenidoCifrado;
        this.claveCifDes = claveCifDes;
        this.estatus = estatus;
    }

    public Integer getIdCorreo() {
        return idCorreo;
    }

    public void setIdCorreo(Integer idCorreo) {
        this.idCorreo = idCorreo;
    }

    public String getContenidoCifrado() {
        return contenidoCifrado;
    }

    public void setContenidoCifrado(String contenidoCifrado) {
        this.contenidoCifrado = contenidoCifrado;
    }

    public String getClaveCifDes() {
        return claveCifDes;
    }

    public void setClaveCifDes(String claveCifDes) {
        this.claveCifDes = claveCifDes;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
