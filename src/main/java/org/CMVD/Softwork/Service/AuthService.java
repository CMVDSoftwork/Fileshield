package org.CMVD.Softwork.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.CMVD.Softwork.DTO.LoginResponse;
import org.CMVD.Softwork.DTO.SesionActiva;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class AuthService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String BASE_URL = "http://localhost:8080/api/auth";

    public CompletableFuture<Boolean> registrar(String nombre, String apellidoP, String apellidoM, String correo, String contrasena) {
        String jsonBody = String.format("""
                {
                    "nombre": "%s",
                    "apellidoPaterno": "%s",
                    "apellidoMaterno": "%s",
                    "correo": "%s",
                    "contrasena": "%s"
                }
                """, nombre, apellidoP, apellidoM, correo, contrasena);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/registro"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> response.statusCode() == 200);
    }

    public CompletableFuture<SesionActiva> login(String correo, String contrasena) {
        String jsonBody = String.format("""
                {
                    "correo": "%s",
                    "contrasena": "%s"
                }
                """, correo, contrasena);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            LoginResponse loginResponse = objectMapper.readValue(response.body(), LoginResponse.class);
                            SesionActiva.iniciarSesion(
                                    loginResponse.getToken(),
                                    loginResponse.getCorreo(),
                                    loginResponse.getNombre(),
                                    loginResponse.getIdUsuario()
                            );

                            return new SesionActiva();
                        } catch (Exception e) {
                            throw new RuntimeException("Error al procesar la respuesta del servidor", e);
                        }
                    } else {
                        throw new RuntimeException("Credenciales inválidas o error del servidor");
                    }
                });
    }

    public CompletableFuture<Boolean> cambiarContrasena(String token, String contrasenaActual, String nuevaContrasena) {
        String jsonBody = String.format("""
                {
                    "contrasenaActual": "%s",
                    "nuevaContrasena": "%s"
                }
                """, contrasenaActual, nuevaContrasena);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/cambiar-contrasena"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> response.statusCode() == 200);
    }

}
