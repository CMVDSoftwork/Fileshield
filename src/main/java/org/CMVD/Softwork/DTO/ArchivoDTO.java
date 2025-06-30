package org.CMVD.Softwork.DTO;

import org.CMVD.Softwork.DTO.Carpeta.CarpetaMonitorizadaDTO;
import org.CMVD.Softwork.DTO.Usuario.UsuarioDTO;

import java.util.Date;

public class ArchivoDTO {
    private Integer idArchivo;
    private String nombreArchivo;
    private String estado;
    private String tipoArchivo;
    private String rutaArchivo;
    private int tamaño;
    private Date fechaSubida;
    private UsuarioDTO usuarioDTO;
    private CarpetaMonitorizadaDTO carpetaMonitorizadaDTO;

    public ArchivoDTO() {}

    public ArchivoDTO(Integer idArchivo, String nombreArchivo, String estado, String tipoArchivo,
                      String rutaArchivo, int tamaño, Date fechaSubida) {
        this.idArchivo = idArchivo;
        this.nombreArchivo = nombreArchivo;
        this.estado = estado;
        this.tipoArchivo = tipoArchivo;
        this.rutaArchivo = rutaArchivo;
        this.tamaño = tamaño;
        this.fechaSubida = fechaSubida;
    }

    public Integer getIdArchivo() { return idArchivo; }
    public void setIdArchivo(Integer idArchivo) { this.idArchivo = idArchivo; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String tipoArchivo) { this.tipoArchivo = tipoArchivo; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public int getTamaño() { return tamaño; }
    public void setTamaño(int tamaño) { this.tamaño = tamaño; }

    public Date getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(Date fechaSubida) { this.fechaSubida = fechaSubida; }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public CarpetaMonitorizadaDTO getCarpetaMonitorizadaDTO() {
        return carpetaMonitorizadaDTO;
    }

    public void setCarpetaMonitorizadaDTO(CarpetaMonitorizadaDTO carpetaMonitorizadaDTO) {
        this.carpetaMonitorizadaDTO = carpetaMonitorizadaDTO;
    }
}
