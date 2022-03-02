package com.qi.hospital.dto.doctor;

import com.qi.hospital.model.dcotor.DoctorTitle;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DoctorUpdateRequest {
    private String name;
    @NotBlank
    private String jobNumber;

    private DoctorTitle title;

    private String intro;

    private String sectionId;
}
