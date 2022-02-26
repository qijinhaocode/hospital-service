package com.qi.hospital.dto.section;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SectionRequest {
    @NotBlank
    private String name;
}
