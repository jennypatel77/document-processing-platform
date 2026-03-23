package com.juniverse.docprocessor.exception;

public class InvalidPayloadException extends NonRetryableJobException {
    public InvalidPayloadException(String message) {
        super(message);
    }
}