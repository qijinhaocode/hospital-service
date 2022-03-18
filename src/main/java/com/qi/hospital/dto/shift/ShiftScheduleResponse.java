package com.qi.hospital.dto.shift;

import com.qi.hospital.dto.doctor.DoctorResponse;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class ShiftScheduleResponse {
    private String doctorJobNumber;
    private LocalDate localDate;
    private Integer morning;
    private Integer afternoon;
    private DoctorResponse doctorResponse;
}