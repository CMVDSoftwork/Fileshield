package org.CMVD.Softwork.DTO.Correo;

import java.util.List;

public class CorreoDTO {
    private Integer idCorreo;
    private String contenidoCifrado;
    private String claveCifDes;
    private String estatus;
    private List<ArchivoCorreoDTO> archivosAdjuntos;

    public CorreoDTO() {
    }

    public CorreoDTO(Integer idCorreo, String contenidoCifrado, String claveCifDes, String estatus, List<ArchivoCorreoDTO> archivosAdjuntos) {
        this.idCorreo = idCorreo;
        this.contenidoCifrado = contenidoCifrado;
        this.claveCifDes = claveCifDes;
        this.estatus = estatus;
        this.archivosAdjuntos = archivosAdjuntos;
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

    public List<ArchivoCorreoDTO> getArchivosAdjuntos() {
        return archivosAdjuntos;
    }

    public void setArchivosAdjuntos(List<ArchivoCorreoDTO> archivosAdjuntos) {
        this.archivosAdjuntos = archivosAdjuntos;
    }
}
