package org.CMVD.Softwork.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.CMVD.Softwork.DTO.DescifradoRequest;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class CifradorAEService {
    private static final String API_URL = "http://localhost:8080/api/cifrado";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public CifradorAEService() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public void descifrarTexto(String textoCifradoBase64, String claveBase64,
                               Consumer<String> callback, Consumer<String> errorCallback) {

        String tokenHeader = SesionActiva.getTokenHeader();
        if (tokenHeader == null) {
            errorCallback.accept("No hay sesión iniciada. Token de autorización no disponible.");
            return;
        }

        DescifradoRequest requestPayload = new DescifradoRequest(textoCifradoBase64, claveBase64);
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(requestPayload);
        } catch (IOException e) {
            errorCallback.accept("Error al serializar la solicitud de descifrado: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/descifrar"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Authorization", tokenHeader)
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> {
                    SwingUtilities.invokeLater(() -> {
                        if (resp.statusCode() == 200) {
                            callback.accept(resp.body());
                        } else {
                            String errorMsg = "Error al descifrar el texto. Código: " + resp.statusCode();
                            if (resp.body() != null && !resp.body().isEmpty()) {
                                errorMsg += ", Mensaje: " + resp.body();
                            }
                            errorCallback.accept(errorMsg);
                        }
                    });
                })
                .exceptionally(e -> {
                    SwingUtilities.invokeLater(() -> {
                        errorCallback.accept("Error de conexión al descifrar el texto: " + e.getMessage());
                    });
                    e.printStackTrace();
                    return null;
                });
    }
}
