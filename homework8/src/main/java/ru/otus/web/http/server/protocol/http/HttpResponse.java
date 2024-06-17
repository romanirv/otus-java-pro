package ru.otus.web.http.server.protocol.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private HttpStatus httpStatus;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public void setStatusCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toRawResponse() {
        StringBuilder resp = new StringBuilder();
        resp.append("HTTP/1.1 ").append(httpStatus.getCode()).append(" ").append(httpStatus.getMessage()).append("\r\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            resp.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }

        if (body != null) {
            resp.append("\r\n");
            resp.append(body);
        }

        resp.append("\r\n");

        return resp.toString();
    }
}
