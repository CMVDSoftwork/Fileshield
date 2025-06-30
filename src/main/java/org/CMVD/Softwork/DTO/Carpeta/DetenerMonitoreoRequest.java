package org.CMVD.Softwork.DTO.Carpeta;

public class DetenerMonitoreoRequest {
    private String ruta;

    public DetenerMonitoreoRequest() {
    }

    public DetenerMonitoreoRequest(String ruta) {
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
