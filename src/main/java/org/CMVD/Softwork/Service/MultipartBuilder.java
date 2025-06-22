package org.CMVD.Softwork.Service;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MultipartBuilder {
    private final String boundary;
    private final List<byte[]> parts = new ArrayList<>();

    public MultipartBuilder(String boundary) {
        this.boundary = boundary;
    }

    public void addPart(String name, String value, String contentType) {
        String part = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"" + name + "\"\r\n" +
                "Content-Type: " + contentType + "\r\n\r\n" +
                value + "\r\n";
        parts.add(part.getBytes(StandardCharsets.UTF_8));
    }

    public void addFile(String name, Path filePath) throws IOException {
        String filename = filePath.getFileName().toString();
        String header = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n";
        parts.add(header.getBytes(StandardCharsets.UTF_8));
        parts.add(Files.readAllBytes(filePath));
        parts.add("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    public HttpRequest.BodyPublisher build() {
        String end = "--" + boundary + "--\r\n";
        parts.add(end.getBytes(StandardCharsets.UTF_8));

        int totalLength = parts.stream().mapToInt(p -> p.length).sum();
        byte[] all = new byte[totalLength];
        int offset = 0;
        for (byte[] p : parts) {
            System.arraycopy(p, 0, all, offset, p.length);
            offset += p.length;
        }
        return HttpRequest.BodyPublishers.ofByteArray(all);
    }
}
