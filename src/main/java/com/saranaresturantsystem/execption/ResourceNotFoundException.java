package com.saranaresturantsystem.execption;

import org.springframework.http.HttpStatus;


public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String resourceName , Long id ) {
        super(HttpStatus.NOT_FOUND,  String.format("%s With Id = %d not found" ,resourceName , id ));
    }
}
