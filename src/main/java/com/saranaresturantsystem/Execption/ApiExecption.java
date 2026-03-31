package com.saranaresturantsystem.Execption;
import lombok.*;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class ApiExecption extends RuntimeException {
    private final HttpStatus status;
    private final String message;
}
