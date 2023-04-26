package com.ecommerce.ecommerce_backend.Exceptions;

import com.ecommerce.ecommerce_backend.Payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<?> WebexchangeBindExceptionHandler(WebExchangeBindException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(e-> {
            String field = e.getField();
            String defaultMessage = e.getDefaultMessage();
            errors.put(field, defaultMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> ApiExceptionHandler(ApiException exception){
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), exception.getStatus());
    }
}
