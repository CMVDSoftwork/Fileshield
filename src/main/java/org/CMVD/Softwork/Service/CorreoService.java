package org.CMVD.Softwork.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.CMVD.Softwork.DTO.CorreoRequest;
import org.CMVD.Softwork.DTO.EnvioCorreoDTO;
import org.CMVD.Softwork.DTO.RecepcionCorreoDTO;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class CorreoService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String BASE_URL = "http://localhost:8080/api/correos";

    public void enviarCorreo(CorreoRequest correo, List<Path> adjuntos, String token, Consumer<Boolean> callback) {
        try {
            String boundary = "----CorreoBoundary" + System.currentTimeMillis();
            var multipartBuilder = new MultipartBuilder(boundary);

            multipartBuilder.addPart("correo", mapper.writeValueAsString(correo), "application/json");

            if (adjuntos != null) {
                for (Path file : adjuntos) {
                    multipartBuilder.addFile("adjuntos", file);
                }
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/enviar"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(multipartBuilder.build())
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(resp -> {
                        if (resp.statusCode() == 200) {
                            SwingUtilities.invokeLater(() -> callback.accept(true));
                        } else {
                            System.err.println("Error al enviar correo. Código: " + resp.statusCode());
                            System.err.println("Mensaje del backend: " + resp.body());
                            SwingUtilities.invokeLater(() -> callback.accept(false));
                        }
                    })
                    .exceptionally(e -> {
                        e.printStackTrace();
                        SwingUtilities.invokeLater(() -> callback.accept(false));
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> callback.accept(false));
        }
    }

    public void obtenerCorreosEnviados(String correoUsuario, Consumer<List<EnvioCorreoDTO>> callback) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/enviados/" + correoUsuario))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> {
                    try {
                        List<EnvioCorreoDTO> lista = mapper.readValue(
                                resp.body(),
                                new TypeReference<List<EnvioCorreoDTO>>() {}
                        );
                        callback.accept(lista);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.accept(List.of());
                    }
                });
    }

    public void obtenerCorreosRecibidos(String correoUsuario, Consumer<List<RecepcionCorreoDTO>> callback) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/recibidos/" + correoUsuario))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> {
                    try {
                        List<RecepcionCorreoDTO> lista = mapper.readValue(
                                resp.body(),
                                new TypeReference<List<RecepcionCorreoDTO>>() {}
                        );
                        callback.accept(lista);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.accept(List.of());
                    }
                });
    }
}
