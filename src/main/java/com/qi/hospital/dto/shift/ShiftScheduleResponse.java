package com.qi.hospital.dto.shift;

import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.model.dcotor.DoctorTitle;
import com.qi.hospital.model.section.Section;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class ShiftScheduleResponse {
    private String id;
    private LocalDate localDate;
    private Integer morning;
    private Integer afternoon;

    private String doctorJobNumber;
    private String doctorName;
    private DoctorTitle doctorTitle;
    private String doctorIntro;
    private String sectionName;
    private Double registrationFee;
}