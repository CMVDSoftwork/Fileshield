package org.CMVD.Softwork.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.CMVD.Softwork.DTO.Correo.CorreoRequest;
import org.CMVD.Softwork.DTO.Correo.EnvioCorreoDTO;
import org.CMVD.Softwork.DTO.Correo.RecepcionCorreoDTO;
import org.CMVD.Softwork.Util.MultipartBuilder;
import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CorreoService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String BASE_URL = "http://localhost:8080/api/correos";

    public void enviarCorreo(CorreoRequest correo, List<Path> adjuntos, String token, Consumer<Map<String, String>> callback, Consumer<String> errorCallback) {
        try {
            if (token == null || token.isEmpty()) {
                errorCallback.accept("Token de autorización no disponible. Inicie sesión.");
                return;
            }
            if (correo == null) {
                errorCallback.accept("Los datos del correo no pueden ser nulos.");
                return;
            }
            List<Path> safeAdjuntos = (adjuntos != null) ? adjuntos : Collections.emptyList();

            String boundary = "----CorreoBoundary" + System.currentTimeMillis();
            var multipartBuilder = new MultipartBuilder(boundary);

            multipartBuilder.addPart("correo", mapper.writeValueAsString(correo), "application/json");

            for (Path file : safeAdjuntos) {
                if (Files.exists(file) && Files.isReadable(file)) {
                    multipartBuilder.addFile("adjuntos", file.getFileName().toString(), Files.readAllBytes(file),
                            Files.probeContentType(file) != null ? Files.probeContentType(file) : "application/octet-stream");
                } else {
                    System.err.println("Advertencia: El archivo adjunto no existe o no se puede leer: " + file.toString());
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
                        SwingUtilities.invokeLater(() -> {
                            if (resp.statusCode() == 200) {
                                try {
                                    Map<String, String> respuesta = mapper.readValue(resp.body(), new TypeReference<>() {});
                                    callback.accept(respuesta);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    errorCallback.accept("Error al parsear la respuesta del servidor: " + e.getMessage());
                                }
                            } else {
                                String errorMsg = "Error al enviar correo. Código: " + resp.statusCode();
                                if (resp.body() != null && !resp.body().isEmpty()) {
                                    errorMsg += ", Mensaje del backend: " + resp.body();
                                }
                                System.err.println(errorMsg);
                                errorCallback.accept(errorMsg);
                            }
                        });
                    })
                    .exceptionally(e -> {
                        e.printStackTrace();
                        SwingUtilities.invokeLater(() -> errorCallback.accept("Error de conexión al enviar correo: " + e.getMessage()));
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> errorCallback.accept("Error inesperado al preparar el envío del correo: " + e.getMessage()));
        }
    }

    public void obtenerCorreosEnviados(String correoUsuario, String token, Consumer<List<EnvioCorreoDTO>> callback, Consumer<String> errorCallback) {
        if (token == null || token.isEmpty()) {
            errorCallback.accept("Token de autorización no disponible. Inicie sesión.");
            return;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/enviados/" + correoUsuario))
                .GET()
                .header("Authorization", "Bearer " + token)
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> {
                    SwingUtilities.invokeLater(() -> {
                        if (resp.statusCode() == 200) {
                            try {
                                List<EnvioCorreoDTO> lista = mapper.readValue(
                                        resp.body(),
                                        new TypeReference<List<EnvioCorreoDTO>>() {}
                                );
                                callback.accept(lista);
                            } catch (Exception e) {
                                e.printStackTrace();
                                errorCallback.accept("Error al parsear la lista de correos enviados: " + e.getMessage());
                                callback.accept(List.of());
                            }
                        } else {
                            String errorMsg = "Error al obtener correos enviados. Código: " + resp.statusCode();
                            if (resp.body() != null && !resp.body().isEmpty()) {
                                errorMsg += ", Mensaje del backend: " + resp.body();
                            }
                            System.err.println(errorMsg);
                            errorCallback.accept(errorMsg);
                            callback.accept(List.of());
                        }
                    });
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> errorCallback.accept("Error de conexión al obtener correos enviados: " + e.getMessage()));
                    return null;
                });
    }

    public void obtenerCorreosRecibidos(String correoUsuario, String token, Consumer<List<RecepcionCorreoDTO>> callback, Consumer<String> errorCallback) {
        if (token == null || token.isEmpty()) {
            errorCallback.accept("Token de autorización no disponible. Inicie sesión.");
            return;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/recibidos/" + correoUsuario))
                .GET()
                .header("Authorization", "Bearer " + token)
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(resp -> {
                    SwingUtilities.invokeLater(() -> {
                        if (resp.statusCode() == 200) {
                            try {
                                List<RecepcionCorreoDTO> lista = mapper.readValue(
                                        resp.body(),
                                        new TypeReference<List<RecepcionCorreoDTO>>() {}
                                );
                                callback.accept(lista);
                            } catch (Exception e) {
                                e.printStackTrace();
                                errorCallback.accept("Error al parsear la lista de correos recibidos: " + e.getMessage());
                                callback.accept(List.of());
                            }
                        } else {
                            String errorMsg = "Error al obtener correos recibidos. Código: " + resp.statusCode();
                            if (resp.body() != null && !resp.body().isEmpty()) {
                                errorMsg += ", Mensaje del backend: " + resp.body();
                            }
                            System.err.println(errorMsg);
                            errorCallback.accept(errorMsg);
                            callback.accept(List.of());
                        }
                    });
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> errorCallback.accept("Error de conexión al obtener correos recibidos: " + e.getMessage()));
                    return null;
                });
    }

    public void descifrarAdjuntoCorreo(Integer idAdjunto, String claveBase64, String token,Consumer<byte[]> callback, Consumer<String> errorCallback) {
        try {
            if (token == null || token.isEmpty()) {
                errorCallback.accept("Token de autorización no disponible para descifrar adjunto. Inicie sesión.");
                return;
            }
            String jsonBody = mapper.writeValueAsString(Map.of("claveBase64", claveBase64));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/adjuntos/descifrar/" + idAdjunto))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                    .thenAccept(resp -> {
                        SwingUtilities.invokeLater(() -> {
                            if (resp.statusCode() == 200) {
                                callback.accept(resp.body());
                            } else {
                                String errorMsg = "Error al descifrar adjunto. Código: " + resp.statusCode();
                                if (resp.body() != null) {
                                    errorMsg += ", Mensaje del backend: " + new String(resp.body(), StandardCharsets.UTF_8);
                                }
                                System.err.println(errorMsg);
                                errorCallback.accept(errorMsg);
                            }
                        });
                    })
                    .exceptionally(e -> {
                        e.printStackTrace();
                        SwingUtilities.invokeLater(() -> errorCallback.accept("Error de conexión al descifrar adjunto: " + e.getMessage()));
                        return null;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> errorCallback.accept("Error inesperado al preparar el descifrado del adjunto: " + e.getMessage()));
        }
    }
}
