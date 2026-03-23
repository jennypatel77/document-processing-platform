package com.juniverse.docprocessor.exception;

public abstract class JobProcessingException extends RuntimeException {

    public JobProcessingException(String message) {
        super(message);
    }

    public JobProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}