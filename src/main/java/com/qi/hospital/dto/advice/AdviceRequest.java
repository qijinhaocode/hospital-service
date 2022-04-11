package com.qi.hospital.dto.advice;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AdviceRequest {
    private String userId;
    @NotBlank
    private String advice;
    @NotNull
    private LocalDateTime createDateTime;
}
