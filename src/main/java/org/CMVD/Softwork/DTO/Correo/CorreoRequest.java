package org.CMVD.Softwork.DTO.Correo;

public class CorreoRequest {
    private String asunto;
    private String cuerpoPlano;
    private String destinatario;
    private String emisor;

    public CorreoRequest() {
    }

    public CorreoRequest(String asunto, String cuerpoPlano, String destinatario, String emisor) {
        this.asunto = asunto;
        this.cuerpoPlano = cuerpoPlano;
        this.destinatario = destinatario;
        this.emisor = emisor;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpoPlano() {
        return cuerpoPlano;
    }

    public void setCuerpoPlano(String cuerpoPlano) {
        this.cuerpoPlano = cuerpoPlano;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }
}
