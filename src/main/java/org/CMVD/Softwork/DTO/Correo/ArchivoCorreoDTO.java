package org.CMVD.Softwork.DTO.Correo;

public class ArchivoCorreoDTO {
    private Integer idArchivoCorreo;
    private String nombreOriginal;

    public ArchivoCorreoDTO() {}

    public ArchivoCorreoDTO(Integer idArchivoCorreo, String nombreOriginal) {
        this.idArchivoCorreo = idArchivoCorreo;
        this.nombreOriginal = nombreOriginal;
    }

    public Integer getIdArchivoCorreo() {
        return idArchivoCorreo;
    }

    public void setIdArchivoCorreo(Integer idArchivoCorreo) {
        this.idArchivoCorreo = idArchivoCorreo;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }
}
