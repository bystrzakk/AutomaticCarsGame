package com.car.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RestApiError {
    private final String code;
    private final String message;
    private final List<FieldError> fieldErrorList;
}
