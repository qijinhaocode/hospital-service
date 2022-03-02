package com.qi.hospital.dto.doctor;

import com.qi.hospital.model.dcotor.DoctorTitle;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DoctorRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String jobNumber;
    @NotNull
    private DoctorTitle title;
    @NotBlank
    private String intro;
    @NotBlank
    private String sectionId;
}
