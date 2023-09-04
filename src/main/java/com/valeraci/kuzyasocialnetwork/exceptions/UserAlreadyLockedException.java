package com.valeraci.kuzyasocialnetwork.exceptions;

public class UserAlreadyLockedException extends RuntimeException {
    public UserAlreadyLockedException(String message) {
        super(message);
    }
}
