package com.car.exception;



import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String NO_MAP_IN_REPOSITORY = "No map in repository exception";
    private static final String REMOVE_MAP = "Map in use exception";
    private static final String INCORRECT_FORMAT_MAP = "Incorrect format map exception";
    private static final String NUMBER_FORMAT_MAP = "Number format map exception";
    private static final String REMOVE_CAR = "Remove car exception";

    @ExceptionHandler(NoMapException.class)
    ResponseEntity<RestApiError> handleNoMapException(NoMapException e) {
        log.error(NO_MAP_IN_REPOSITORY, e);

        return new ResponseEntity<>(new RestApiError(BAD_REQUEST.toString(),
                NO_MAP_IN_REPOSITORY, null), BAD_REQUEST);
    }

    @ExceptionHandler(RemoveMapException.class)
    ResponseEntity<RestApiError> handleRemoveMapException(RemoveMapException e) {
        log.error(REMOVE_MAP, e);

        return new ResponseEntity<>(new RestApiError(BAD_REQUEST.toString(),
                REMOVE_MAP, null), BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectFormatMapException.class)
    ResponseEntity<RestApiError> handleIncorrectFormatMapException(IncorrectFormatMapException e) {
        log.error(INCORRECT_FORMAT_MAP, e);

        return new ResponseEntity<>(new RestApiError(BAD_REQUEST.toString(),
                INCORRECT_FORMAT_MAP, null), BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    ResponseEntity<RestApiError> handleIncorrectFormatMapException(NumberFormatException e) {
        log.error(NUMBER_FORMAT_MAP, e);

        return new ResponseEntity<>(new RestApiError(BAD_REQUEST.toString(),
                NUMBER_FORMAT_MAP, null), BAD_REQUEST);
    }

    @ExceptionHandler(RemoveCarException.class)
    ResponseEntity<RestApiError> handleRemoveCarException(RemoveCarException e) {
        log.error(REMOVE_CAR, e);

        return new ResponseEntity<>(new RestApiError(BAD_REQUEST.toString(),
                REMOVE_CAR, null), BAD_REQUEST);
    }
}
