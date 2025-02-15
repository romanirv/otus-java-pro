package ru.otus.serialization.exceptions;

public class SerializeError extends RuntimeException {
    public SerializeError(String message) {
        super(message);
    }
}
