package com.juniverse.docprocessor.exception;

public class MissingRequiredFieldException extends NonRetryableJobException {
    public MissingRequiredFieldException(String message) {
        super(message);
    }
}