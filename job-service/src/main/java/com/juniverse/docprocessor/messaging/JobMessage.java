package com.juniverse.docprocessor.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobMessage {
    private Long jobId;
    private String fileName;
    private Integer retryCount = 0;
    private String traceId;
}