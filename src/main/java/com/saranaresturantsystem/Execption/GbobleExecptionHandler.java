package com.saranaresturantsystem.Execption;

import com.saranaresturantsystem.DTO.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GbobleExecptionHandler {

    @ExceptionHandler(value = ApiExecption.class)
    public ResponseEntity<?> handleApiExecption(ApiExecption e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .succeess(false)
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation failed")
                .payload(errors)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundExecption.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundExecption ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .succeess(false)
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .succeess(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred: " + ex.getMessage())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
