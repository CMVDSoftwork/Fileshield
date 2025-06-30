package org.CMVD.Softwork.DTO.Correo;

import org.CMVD.Softwork.DTO.Usuario.UsuarioDTO;

import java.util.Date;

public class EnvioCorreoDTO {
    private Integer idEnvioCorreo;
    private Date fechaEnvio;
    private UsuarioDTO usuarioEmisorDTO;
    private CorreoDTO correoDTO;

    public EnvioCorreoDTO() {
    }

    public EnvioCorreoDTO(Integer idEnvioCorreo, Date fechaEnvio, UsuarioDTO usuarioEmisorDTO, CorreoDTO correoDTO) {
        this.idEnvioCorreo = idEnvioCorreo;
        this.fechaEnvio = fechaEnvio;
        this.usuarioEmisorDTO = usuarioEmisorDTO;
        this.correoDTO = correoDTO;
    }

    public Integer getIdEnvioCorreo() {
        return idEnvioCorreo;
    }

    public void setIdEnvioCorreo(Integer idEnvioCorreo) {
        this.idEnvioCorreo = idEnvioCorreo;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public UsuarioDTO getUsuarioEmisorDTO() {
        return usuarioEmisorDTO;
    }

    public void setUsuarioEmisorDTO(UsuarioDTO usuarioEmisorDTO) {
        this.usuarioEmisorDTO = usuarioEmisorDTO;
    }

    public CorreoDTO getCorreoDTO() {
        return correoDTO;
    }

    public void setCorreoDTO(CorreoDTO correoDTO) {
        this.correoDTO = correoDTO;
    }
}
