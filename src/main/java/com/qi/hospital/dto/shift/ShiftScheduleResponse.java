package com.qi.hospital.dto.shift;

import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.model.section.Section;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class ShiftScheduleResponse {
    private String id;
    private Section section;
    private String doctorJobNumber;
    private LocalDate localDate;
    private Integer morning;
    private Integer afternoon;
    private DoctorResponse doctorResponse;
}