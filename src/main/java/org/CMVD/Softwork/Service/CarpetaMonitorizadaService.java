package org.CMVD.Softwork.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.CMVD.Softwork.DTO.Carpeta.CarpetaMonitorizadaDTO;
import org.CMVD.Softwork.DTO.Carpeta.DetenerMonitoreoRequest;
import org.CMVD.Softwork.DTO.Carpeta.MonitoreoRequest;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Consumer;

public class CarpetaMonitorizadaService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String API_URL = "http://localhost:8080/api/carpetas";

    public void registrarCarpeta(CarpetaMonitorizadaDTO dto, Consumer<CarpetaMonitorizadaDTO> callback) {
        try {
            String json = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/registrar"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SesionActiva.getTokenHeader())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(resp -> {
                        try {
                            if (resp.statusCode() == 200) {
                                CarpetaMonitorizadaDTO result = mapper.readValue(resp.body(), CarpetaMonitorizadaDTO.class);
                                callback.accept(result);
                            } else {
                                callback.accept(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.accept(null);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            callback.accept(null);
        }
    }

    public void obtenerCarpetas(Consumer<List<CarpetaMonitorizadaDTO>> callback) {
        int idUsuario = SesionActiva.getIdUsuario();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/usuario/" + idUsuario))
                .header("Authorization", SesionActiva.getTokenHeader())
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> {
                    if (resp.statusCode() != 200) {
                        callback.accept(List.of());
                        return;
                    }
                    try {
                        List<CarpetaMonitorizadaDTO> lista = mapper.readValue(
                                resp.body(),
                                mapper.getTypeFactory().constructCollectionType(List.class, CarpetaMonitorizadaDTO.class)
                        );
                        callback.accept(lista);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.accept(List.of());
                    }
                });
    }

    public void eliminarCarpeta(Integer id, Runnable onSuccess) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Authorization", SesionActiva.getTokenHeader())
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> {
                    System.out.println("🔁 Código de respuesta al eliminar: " + resp.statusCode());
                    System.out.println("🔁 Cuerpo de respuesta: " + resp.body());

                    if (resp.statusCode() == 200 || resp.statusCode() == 204) {
                        onSuccess.run();
                    } else {
                        System.err.println("Error al eliminar carpeta. Código: " + resp.statusCode());
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    public void iniciarMonitoreo(String ruta, String contrasena, Integer idUsuario, Consumer<Boolean> callback) {
        try {
            MonitoreoRequest payload = new MonitoreoRequest(idUsuario, contrasena, ruta);
            String json = mapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/monitorear"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SesionActiva.getTokenHeader())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Código de respuesta: " + response.statusCode());
                        System.out.println("Cuerpo de respuesta: " + response.body());

                        boolean exito = response.statusCode() == 200;
                        callback.accept(exito);
                    })
                    .exceptionally(e -> {
                        e.printStackTrace();
                        callback.accept(false);
                        return null;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            callback.accept(false);
        }
    }

    public void detenerMonitoreo(String ruta, Consumer<Boolean> callback) {
        try {
            DetenerMonitoreoRequest payload = new DetenerMonitoreoRequest(ruta);
            String json = mapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/detener"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SesionActiva.getTokenHeader())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Código de respuesta: " + response.statusCode());
                        System.out.println("Cuerpo de respuesta: " + response.body());
                        boolean exito = response.statusCode() == 200;
                        callback.accept(exito);
                    })
                    .exceptionally(e -> {
                        e.printStackTrace();
                        callback.accept(false);
                        return null;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            callback.accept(false);
        }
    }
}
