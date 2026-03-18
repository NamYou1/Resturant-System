package com.saranaresturantsystem.Execption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private  final HttpStatus status ;
    private  final String message ;
}
