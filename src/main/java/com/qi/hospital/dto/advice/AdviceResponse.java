package com.qi.hospital.dto.advice;

import com.qi.hospital.dto.user.UserResponse;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class AdviceResponse {
    private UserResponse userResponse;
    private String advice;
    private LocalDateTime createTime;
}
