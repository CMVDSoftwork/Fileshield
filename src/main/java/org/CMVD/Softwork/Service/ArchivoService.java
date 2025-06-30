package org.CMVD.Softwork.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.CMVD.Softwork.DTO.ArchivoDTO;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArchivoService {
    private static final String API_URL = "http://localhost:8080/api/archivos";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ArchivoService() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public List<ArchivoDTO> obtenerTodosLosArchivos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/todos"))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", SesionActiva.getTokenHeader())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        if (responseBody == null || responseBody.isEmpty()) {
            return new ArrayList<>();
        }

        return mapper.readValue(responseBody, new TypeReference<List<ArchivoDTO>>() {});
    }

    public List<ArchivoDTO> obtenerArchivosPorCarpeta(Integer idCarpetaMonitorizada) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/carpeta/" + idCarpetaMonitorizada))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", SesionActiva.getTokenHeader())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<ArchivoDTO>>() {});
    }

    public String descifrarArchivo(Integer idArchivo, String claveCifradoPersonalBase64) throws IOException, InterruptedException {
        String tokenHeader = SesionActiva.getTokenHeader();
        if (tokenHeader == null) {
            throw new IllegalStateException("No hay sesión iniciada. Token de autorización no disponible.");
        }
        if (claveCifradoPersonalBase64 == null || claveCifradoPersonalBase64.isEmpty()) {
            throw new IllegalArgumentException("La clave de cifrado personal no puede ser nula o vacía.");
        }
        if (idArchivo == null) {
            throw new IllegalArgumentException("El ID del archivo no puede ser nulo.");
        }

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("idArchivo", idArchivo);
        requestPayload.put("clavePersonal", claveCifradoPersonalBase64);

        String jsonBody = mapper.writeValueAsString(requestPayload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/descifrar"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Authorization", tokenHeader)
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() == 200) {
            byte[] archivoDescifradoBytes = response.body();
            if (archivoDescifradoBytes == null || archivoDescifradoBytes.length == 0) {
                return "Error: El servidor no devolvió datos para el archivo descifrado.";
            }
            String nombreSugerido = "descifrado_sin_nombre"; // Fallback
            Optional<String> contentDisposition = response.headers().firstValue("Content-Disposition");
            if (contentDisposition.isPresent()) {
                Pattern pattern = Pattern.compile("filename=\"([^\"]+)\"");
                Matcher matcher = pattern.matcher(contentDisposition.get());
                if (matcher.find()) {
                    nombreSugerido = matcher.group(1);
                }
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Archivo Descifrado");
            fileChooser.setSelectedFile(new File(nombreSugerido));

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                    fos.write(archivoDescifradoBytes);
                    return "Archivo '" + fileToSave.getName() + "' descifrado y guardado correctamente.";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error al guardar el archivo descifrado en el equipo local: " + e.getMessage();
                }
            } else {
                return "Descifrado completado, pero el usuario canceló la descarga.";
            }

        } else {
            String errorMsg = "Fallo al descifrar el archivo. Estado: " + response.statusCode();
            if (response.body() != null && response.body().length > 0) {
                errorMsg += ", Mensaje: " + new String(response.body(), StandardCharsets.UTF_8);
            }
            throw new IOException(errorMsg);
        }
    }

    public void eliminarArchivo(Integer idArchivo) throws IOException, InterruptedException {
        String tokenHeader = SesionActiva.getTokenHeader();
        if (tokenHeader == null) {
            throw new IllegalStateException("No hay sesión iniciada. Token de autorización no disponible.");
        }
        if (idArchivo == null) {
            throw new IllegalArgumentException("El ID del archivo no puede ser nulo para eliminar.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + idArchivo))
                .DELETE()
                .header("Authorization", tokenHeader)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 204) {
            String errorMsg = "Fallo al eliminar el archivo. Estado: " + response.statusCode();
            if (response.body() != null && !response.body().isEmpty()) {
                errorMsg += ", Mensaje: " + response.body();
            }
            throw new IOException(errorMsg);
        }
    }
}
