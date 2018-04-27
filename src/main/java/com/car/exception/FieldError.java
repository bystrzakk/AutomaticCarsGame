package com.car.exception;

import lombok.Getter;

@Getter
public class FieldError {
    private String field;
    private String message;

    public FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
