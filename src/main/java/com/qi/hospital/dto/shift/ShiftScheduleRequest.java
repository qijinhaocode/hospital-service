package com.qi.hospital.dto.shift;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class ShiftScheduleRequest {
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}