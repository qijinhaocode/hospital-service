package com.qi.hospital.dto.section;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SectionUpdateRequest {
    @NotBlank
    private String originalName;
    @NotBlank
    private String newName;
}
