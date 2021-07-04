package com.takeaway.game.of.three.rest.errorhandlers;


import com.takeaway.game.of.three.rest.errorhandlers.exceptions.models.ErrorResponse;
import com.takeaway.game.of.three.rest.errorhandlers.exceptions.models.NotAvailableOpponentException;
import com.takeaway.game.of.three.rest.errorhandlers.exceptions.models.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom exception handler for REst api
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(HttpServletRequest req,
                                                                Exception e) {
        String msg = "Technical Error. Please contact administrator.";
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().errorCode("INTERNAL_SERVER_ERROR").message(msg).build());
    }

    @ExceptionHandler(NotAvailableOpponentException.class)
    public ResponseEntity<ErrorResponse> handleNotAvailableOpponentException(HttpServletRequest req,
                                                                             NotAvailableOpponentException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                .message(e.getMessage())
                .build());
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(HttpServletRequest req,
                                                                            ConstraintViolationException e) {
        Set<ConstraintViolation<?>> set = e.getConstraintViolations();
        List<ValidationError> errors = new ArrayList<>();

        if(set!=null){
            errors = set.stream().map(s->ValidationError.builder()
                    .invalidValue(String.valueOf(s.getInvalidValue()))
                    .message(s.getMessage())
                    .field(((PathImpl)s.getPropertyPath()).getLeafNode().getName())
                    .build()).collect(Collectors.toList());
        }
        log.error(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errors(errors)
                        .errorCode("BAD_REQUEST")
                        .message("Invalid request").build());
    }



    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest req,
                                                                               MethodArgumentNotValidException e) {


      List<FieldError> fieldErrors = e.getFieldErrors();
        List<ValidationError> errors = fieldErrors.stream().map(f->ValidationError.builder()
                .field(f.getField())
                .message(f.getDefaultMessage())
                .invalidValue(String.valueOf(f.getRejectedValue()))
                .build()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errors(errors)
                        .errorCode("BAD_REQUEST")
                        .message("Invalid request").build());
    }
}
