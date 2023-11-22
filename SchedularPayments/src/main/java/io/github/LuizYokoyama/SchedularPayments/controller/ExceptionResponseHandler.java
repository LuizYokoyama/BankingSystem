package io.github.LuizYokoyama.SchedularPayments.controller;

import io.github.LuizYokoyama.SchedularPayments.exceptions.BadRequestRuntimeException;
import io.github.LuizYokoyama.SchedularPayments.exceptions.NotFoundRuntimeException;
import io.github.LuizYokoyama.SchedularPayments.exceptions.PreviousDateRuntimeException;
import io.github.LuizYokoyama.SchedularPayments.exceptions.ValueZeroRuntimeException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict( ConstraintViolationException ex ) {
        Map<String, String> constraintViolations = new HashMap<>();
        ex.getConstraintViolations().forEach((constraintViolation) -> {
            String message = constraintViolation.getMessage();
            String field = constraintViolation.getPropertyPath().toString();
            field = field.substring(field.lastIndexOf('.')+1); // returns the last component of the path
            constraintViolations.put(field, message);
        });
        return ResponseEntity.badRequest().body(constraintViolations);
    }

    @ExceptionHandler(value = {ValueZeroRuntimeException.class})
    protected ResponseEntity<Object> handleValueZeroException(
            ValueZeroRuntimeException ex ) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = {NotFoundRuntimeException.class})
    protected ResponseEntity<Object> handleNotFoundException(
            NotFoundRuntimeException ex ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = {PreviousDateRuntimeException.class})
    protected ResponseEntity<Object> handlePreviousDateException(
            PreviousDateRuntimeException ex ) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = {BadRequestRuntimeException.class})
    protected ResponseEntity<Object> handleBadRequestException(
            BadRequestRuntimeException ex ) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}