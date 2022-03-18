package com.qi.hospital.dto.shift;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ShiftScheduleRequest {
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}