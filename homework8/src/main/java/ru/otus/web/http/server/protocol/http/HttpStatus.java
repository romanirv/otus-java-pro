package ru.otus.web.http.server.protocol.http;

public enum HttpStatus {
    OK(200, "OK"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    BAD_REQUEST(400, "Bad Request"),
    NO_CONTENT(204, "No Content");

    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
