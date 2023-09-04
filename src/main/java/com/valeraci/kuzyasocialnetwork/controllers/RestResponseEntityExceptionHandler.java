package com.valeraci.kuzyasocialnetwork.controllers;

import com.valeraci.kuzyasocialnetwork.exceptions.EmailAlreadyExistsException;
import com.valeraci.kuzyasocialnetwork.exceptions.InvalidTokenException;
import com.valeraci.kuzyasocialnetwork.exceptions.UserAlreadyLockedException;
import com.valeraci.kuzyasocialnetwork.exceptions.UserNotBlockedException;
import com.valeraci.kuzyasocialnetwork.exceptions.UserNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException exception) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        List<String> errors = exception.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Map<String, List<String>> responseBody = new HashMap<>();
        responseBody.put("errors", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleLockedException(InvalidTokenException exception){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(UserAlreadyLockedException.class)
    public ResponseEntity<Object> handleLockAlreadyExistsException(UserAlreadyLockedException exception){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Object> handleLockedException(LockedException exception) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.LOCKED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(UserNotBlockedException.class)
    public ResponseEntity<Object> handleUserNotBlockedException(UserNotBlockedException exception) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }
}
