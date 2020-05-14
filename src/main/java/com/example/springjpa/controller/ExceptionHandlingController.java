package com.example.springjpa.controller;

import com.example.springjpa.model.ErrorDto;
import com.example.springjpa.exceptions.CustomerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlingController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ResponseStatus
    @ExceptionHandler(value= CustomerNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCustomerNotFoundException(CustomerNotFoundException ex) {

        logger.error("Exception: CUSTOMER_NOT_FOUND " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .errorCode("CUSTOMER_NOT_FOUND")
                .errorMessage(ex.getMessage())
                .time(OffsetDateTime.now())
                .build());
    }

    @ExceptionHandler(value= DataAccessException.class)
    public ResponseEntity<ErrorDto> handleDataAccessException(DataAccessException ex) {

        logger.error("Exception: DATA_ACCESS_EXCEPTION " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDto.builder()
                 .status(HttpStatus.INTERNAL_SERVER_ERROR)
                 .errorCode("DATA_ACCESS_EXCEPTION")
                 .errorMessage("Exception while accessing Database")
                 .time(OffsetDateTime.now())
                 .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> handleValidationError(ConstraintViolationException ex) {

        List<String> errorList = new ArrayList<>(ex.getConstraintViolations().size());
        ex.getConstraintViolations().forEach(
                constraintViolation -> {
                    errorList.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage());
                }
        );
        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodValidationError(MethodArgumentNotValidException ex) {

        ErrorDto errorDto = ErrorDto.builder()
                            .errorCode("METHOD_ARGUMENT_INVALID")
                            .errorMessage(ex.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorDto);
    }
}
