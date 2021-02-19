package com.udacity.vehicles.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ManufacturerNotFoundException extends RuntimeException {

    public ManufacturerNotFoundException() {
    }

    public ManufacturerNotFoundException(String message) {
        super(message);
    }
}
