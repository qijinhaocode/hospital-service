package com.qi.hospital.dto.shift;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Data
public class ManuallyCreateShiftScheduleRequest {
    @NotNull
    LocalDate startDate;
    @NotNull
    LocalDate endDate;
    @NotBlank
    String doctorJobNumber;
    private Integer morning;
    private Integer afternoon;
}
