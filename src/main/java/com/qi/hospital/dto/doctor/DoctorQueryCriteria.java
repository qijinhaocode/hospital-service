package com.qi.hospital.dto.doctor;

import com.qi.hospital.model.dcotor.DoctorTitle;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class DoctorQueryCriteria {
    private String name;
    private String jobNumber;
    private DoctorTitle title;
    private String sectionId;
}
