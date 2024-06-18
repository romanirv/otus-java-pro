package ru.otus.web.http.server.protocol.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private HttpStatus httpStatus;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public void setStatusCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ").append(httpStatus.getCode()).append(" ")
               .append(httpStatus.getMessage()).append("\r\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }

        stream.write(builder.toString().getBytes(StandardCharsets.UTF_8));

        if (body != null) {
            stream.write("\r\n".getBytes(StandardCharsets.UTF_8));
            stream.write(body);
        }

        stream.write("\r\n".getBytes(StandardCharsets.UTF_8));
        stream.flush();
    }
}
