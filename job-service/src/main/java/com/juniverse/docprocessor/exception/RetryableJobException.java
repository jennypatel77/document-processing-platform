package com.juniverse.docprocessor.exception;

public class RetryableJobException extends JobProcessingException {

    public RetryableJobException(String message) {
        super(message);
    }

    public RetryableJobException(String message, Throwable cause) {
        super(message, cause);
    }
}