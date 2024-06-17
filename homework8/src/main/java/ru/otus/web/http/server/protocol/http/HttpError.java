package ru.otus.web.http.server.protocol.http;

public class HttpError extends Exception {
    public HttpError(String message) {
        super(message);
    }
}
