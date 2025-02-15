package ru.otus.java.pro.spring.app.exceptions_handling;

import java.time.LocalDateTime;
import java.util.List;

public class ValidationErrorDto {
    private String code;
    private String message;
    private List<ValidationFieldErrorDto> errors;
    private LocalDateTime dateTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ValidationFieldErrorDto> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationFieldErrorDto> errors) {
        this.errors = errors;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public ValidationErrorDto(String code, String message, List<ValidationFieldErrorDto> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.dateTime = LocalDateTime.now();
    }
}
