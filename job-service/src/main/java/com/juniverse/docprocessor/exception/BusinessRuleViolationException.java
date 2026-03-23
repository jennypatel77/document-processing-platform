package com.juniverse.docprocessor.exception;

public class BusinessRuleViolationException extends NonRetryableJobException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}