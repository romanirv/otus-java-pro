package ru.otus.java.pro.spring.app.exceptions_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> catchResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ErrorDto("RESOURCE_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ValidationErrorDto> catchValidationException(ValidationException e) {
        return new ResponseEntity<>(
                new ValidationErrorDto(
                        e.getCode(),
                        e.getMessage(),
                        e.getErrors().stream().map(ve -> new ValidationFieldErrorDto(ve.getField(), ve.getMessage())).collect(Collectors.toUnmodifiableList())
                ),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @ExceptionHandler(value = BusinessLogicException.class)
    public ResponseEntity<ErrorDto> catchBusinessLoginException(BusinessLogicException e) {
        return new ResponseEntity<>(new ErrorDto(e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
