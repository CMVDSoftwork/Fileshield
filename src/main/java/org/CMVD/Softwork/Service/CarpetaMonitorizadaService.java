package org.CMVD.Softwork.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.CMVD.Softwork.DTO.CarpetaMonitorizadaDTO;
import org.CMVD.Softwork.DTO.SesionActiva;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/monitorear"))
                .header("Authorization", SesionActiva.getTokenHeader())
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> {
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

    public void eliminarCarpeta(Long id, Runnable onSuccess) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Authorization", SesionActiva.getTokenHeader())
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenAccept(resp -> {
                    if (resp.statusCode() == 200) onSuccess.run();
                });
    }

    public void iniciarMonitoreo(String ruta, String contrasena, Integer idUsuario, Consumer<Boolean> callback) {
        try {
            Map<String, Object> payload = Map.of(
                    "ruta", ruta,
                    "contrasena", contrasena,
                    "idUsuario", idUsuario
            );

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
}
