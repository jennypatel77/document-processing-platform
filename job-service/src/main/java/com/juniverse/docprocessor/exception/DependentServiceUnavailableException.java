package com.juniverse.docprocessor.exception;

public class DependentServiceUnavailableException extends RetryableJobException {
    public DependentServiceUnavailableException(String message) {
        super(message);
    }
}