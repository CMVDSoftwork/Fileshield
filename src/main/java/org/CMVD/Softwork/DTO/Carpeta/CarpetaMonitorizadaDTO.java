package org.CMVD.Softwork.DTO.Carpeta;

import org.CMVD.Softwork.DTO.Usuario.UsuarioDTO;

public class CarpetaMonitorizadaDTO {
    private Integer idCarpetaMonitorizada;
    private String ruta;
    private String estado;
    private UsuarioDTO usuarioDTO;

    public CarpetaMonitorizadaDTO() {
    }

    public CarpetaMonitorizadaDTO(Integer idCarpetaMonitorizada, String ruta, String estado, UsuarioDTO usuarioDTO) {
        this.idCarpetaMonitorizada = idCarpetaMonitorizada;
        this.ruta = ruta;
        this.estado = estado;
        this.usuarioDTO = usuarioDTO;
    }

    public Integer getIdCarpetaMonitorizada() {
        return idCarpetaMonitorizada;
    }

    public void setIdCarpetaMonitorizada(Integer idCarpetaMonitorizada) {
        this.idCarpetaMonitorizada = idCarpetaMonitorizada;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }
}
