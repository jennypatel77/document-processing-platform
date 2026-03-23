package com.juniverse.docprocessor.exception;

public class TemporaryNetworkException extends RetryableJobException {
    public TemporaryNetworkException(String message) {
        super(message);
    }
}