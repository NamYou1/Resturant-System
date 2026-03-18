package com.saranaresturantsystem.Execption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GbobleExecptionHandler {
    @ExceptionHandler(value = ApiExecption.class)
    public ResponseEntity<?> handleApiExecption(ApiExecption e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
    }
}
