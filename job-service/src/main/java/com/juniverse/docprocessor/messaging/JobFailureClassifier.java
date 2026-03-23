package com.juniverse.docprocessor.messaging;

import com.juniverse.docprocessor.exception.NonRetryableJobException;
import com.juniverse.docprocessor.exception.RetryableJobException;
import org.springframework.stereotype.Component;

@Component
public class JobFailureClassifier {

    public boolean isRetryable(Exception exception) {
        return exception instanceof RetryableJobException;
    }

    public boolean isNonRetryable(Exception exception) {
        return exception instanceof NonRetryableJobException;
    }
}