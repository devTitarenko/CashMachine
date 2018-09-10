package com.titarenko.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.titarenko.entity.ApiError;
import com.titarenko.service.CardServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CardServiceImpl.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .forEach(errors::add);
        ex.getBindingResult().getGlobalErrors().stream()
                .map(error -> error.getObjectName() + ": " + error.getDefaultMessage())
                .forEach(errors::add);
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handleValidationException(Exception exc) {
//        LOGGER.error(exc.getMessage(), exc);
//        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
//    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        LOGGER.error(ex.getLocalizedMessage());
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getLocalizedMessage(), "Wrong JSON");
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    protected ResponseEntity<Object> handleInvalidTemplateInput(RuntimeException ex, WebRequest webRequest) {
        LOGGER.error(ex.getMessage());
        return handleExceptionInternal(ex, "".toString(), new HttpHeaders(), BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(value = JsonMappingException.class)
    protected ResponseEntity<Object> handleJsonMappingException(RuntimeException ex, WebRequest webRequest) {
        LOGGER.error(ex.getMessage());
        return handleExceptionInternal(ex, "".toString(), new HttpHeaders(), BAD_REQUEST, webRequest);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
