package com.qi.hospital.dto.doctor;

import com.qi.hospital.model.dcotor.DoctorTitle;
import com.qi.hospital.model.section.Section;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private String name;
    private String jobNumber;
    private DoctorTitle title;
    private String intro;
    private Section section;
}
