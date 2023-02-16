package com.hecaires.rest.controllers;

import com.hecaires.rest.exceptions.ResourceNotFoundException;
import com.hecaires.rest.payloads.response.ErrorMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<ErrorMessageResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        logger.warn("Access Denied for the WebRequest: {}", request.getDescription(true));

        return new ResponseEntity<ErrorMessageResponse>(errorMessageResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    public ResponseEntity<ErrorMessageResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<ErrorMessageResponse>(errorMessageResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<ErrorMessageResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<ErrorMessageResponse>(errorMessageResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = { HttpMediaTypeNotSupportedException.class })
    public ResponseEntity<ErrorMessageResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<ErrorMessageResponse>(errorMessageResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(value = { BindException.class })
    public ResponseEntity<ErrorMessageResponse> handleBindException(BindException ex, WebRequest request) {
        final List<String> field_errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                field_errors.toString(),
                ex.getMessage()
        );

        return new ResponseEntity<ErrorMessageResponse>(errorMessageResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ErrorMessageResponse> handleExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        ex.printStackTrace();

        return new ResponseEntity<ErrorMessageResponse>(errorMessageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}