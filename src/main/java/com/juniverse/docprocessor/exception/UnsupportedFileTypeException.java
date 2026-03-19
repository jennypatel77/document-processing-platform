package com.juniverse.docprocessor.exception;

public class UnsupportedFileTypeException extends NonRetryableJobException {
    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}