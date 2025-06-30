package org.CMVD.Softwork.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.CMVD.Softwork.DTO.Correo.CorreoDTO;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class EnlaceService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String BASE_URL = "http://localhost:8080/api/enlaces";

    public void validarEnlaceYObtenerClave(String token, String correoUsuario, Consumer<Optional<CorreoDTO>> callback, Consumer<Integer> errorCallback) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + token + "?correoUsuario=" + correoUsuario))
                .header("Authorization", SesionActiva.getTokenHeader())
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> SwingUtilities.invokeLater(() -> {
                    if (resp.statusCode() == 200) {
                        try {
                            CorreoDTO correoDTO = mapper.readValue(resp.body(), CorreoDTO.class);
                            callback.accept(Optional.of(correoDTO));
                        } catch (IOException e) {
                            System.err.println("Error de parseo al obtener CorreoDTO: " + e.getMessage());
                            e.printStackTrace();
                            callback.accept(Optional.empty());
                        }
                    } else {
                        System.err.println("Error al validar enlace. Código: " + resp.statusCode() + ", Mensaje: " + resp.body());
                        errorCallback.accept(resp.statusCode());
                    }
                }))
                .exceptionally(e -> {
                    System.err.println("Error de conexión al validar enlace: " + e.getMessage());
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> errorCallback.accept(-1));
                    return null;
                });
    }

    public CompletableFuture<String> obtenerHTMLValidacionEnlace(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + token + "/validar"))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(e -> {
                    System.err.println("Error al obtener HTML de validación: " + e.getMessage());
                    e.printStackTrace();
                    return "<html><body><h1>Error al cargar la página de validación.</h1></body></html>";
                });
    }
}
