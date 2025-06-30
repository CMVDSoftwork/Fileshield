package org.CMVD.Softwork.DTO.Correo;

import org.CMVD.Softwork.DTO.Usuario.UsuarioDTO;

import java.util.Date;

public class RecepcionCorreoDTO {
    private Integer idRecepcionCorreo;
    private Date fechaRecepcion;
    private UsuarioDTO usuarioRecepcionDTO;
    private EnvioCorreoDTO envioRecepcionDTO;

    public RecepcionCorreoDTO() {
    }

    public RecepcionCorreoDTO(Integer idRecepcionCorreo, Date fechaRecepcion, UsuarioDTO usuarioRecepcionDTO, EnvioCorreoDTO envioRecepcionDTO) {
        this.idRecepcionCorreo = idRecepcionCorreo;
        this.fechaRecepcion = fechaRecepcion;
        this.usuarioRecepcionDTO = usuarioRecepcionDTO;
        this.envioRecepcionDTO = envioRecepcionDTO;
    }

    public Integer getIdRecepcionCorreo() {
        return idRecepcionCorreo;
    }

    public void setIdRecepcionCorreo(Integer idRecepcionCorreo) {
        this.idRecepcionCorreo = idRecepcionCorreo;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public UsuarioDTO getUsuarioRecepcionDTO() {
        return usuarioRecepcionDTO;
    }

    public void setUsuarioRecepcionDTO(UsuarioDTO usuarioRecepcionDTO) {
        this.usuarioRecepcionDTO = usuarioRecepcionDTO;
    }

    public EnvioCorreoDTO getEnvioRecepcionDTO() {
        return envioRecepcionDTO;
    }

    public void setEnvioRecepcionDTO(EnvioCorreoDTO envioRecepcionDTO) {
        this.envioRecepcionDTO = envioRecepcionDTO;
    }
}
