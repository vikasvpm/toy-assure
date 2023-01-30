package org.learning.assure.controller.handler;

import org.learning.assure.exception.ApiException;
import org.learning.assure.model.response.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleUserException(ApiException apiException) {
        ExceptionResponse response = new ExceptionResponse(apiException.getErrors());
        return new ResponseEntity<>(apiException.getErrors(),HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Object> handleConstraintException(ConstraintViolationException constraintViolationException) {
//        ExceptionResponse response = new ExceptionResponse(constraintViolationException.getMessage());
//        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//    }
//
//
//    @ExceptionHandler(SQLException.class)
//    protected ResponseEntity<Object> handleSQLException(
//            Exception ex) {
//        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
//        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<Object> handleException(
//            Exception ex) {
//        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
//        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    protected ResponseEntity<Object> handleRuntimeException(
//            RuntimeException ex) {
//        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
//        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
//                .map(error -> "Field '" + error.getField() + "' " + error.getDefaultMessage())
//                .collect(Collectors.toList());
//        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
//    }
// TODO return list of exceptions
}
