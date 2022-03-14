package com.qi.hospital.dto.registrationFee;

import com.qi.hospital.model.dcotor.DoctorTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationFeeUpdateRequest {
    @NotNull
    private DoctorTitle doctorTitle;
    @NotNull
    private Double registrationFee;
}
