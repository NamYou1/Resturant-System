package com.saranaresturantsystem.Execption;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiExecption extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public ApiExecption(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
