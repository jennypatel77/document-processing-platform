package com.juniverse.docprocessor.exception;

public class JobTimeoutException extends RetryableJobException {
    public JobTimeoutException(String message) {
        super(message);
    }
}