package com.juniverse.docprocessor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateJobRequest {
    @NotBlank(message = "fileName is required")
    private String fileName;
}
