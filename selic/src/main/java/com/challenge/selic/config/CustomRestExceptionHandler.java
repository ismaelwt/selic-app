package com.challenge.selic.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        List<String> messages = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            messages.add(violation.getMessage());
            errors.add(violation.getRootBeanClass().getName() +" | "+ violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ExceptionResponseWrapper error =  new ExceptionResponseWrapper(HttpStatus.BAD_REQUEST, messages, errors);
        return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
    }
}
