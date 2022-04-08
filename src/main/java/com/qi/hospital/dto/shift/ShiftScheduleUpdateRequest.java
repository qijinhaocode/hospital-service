package com.qi.hospital.dto.shift;

import com.qi.hospital.model.appointment.AppointmentTime;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ShiftScheduleUpdateRequest {
    @NotBlank
    private String id;
    @NotNull
    private AppointmentTime appointmentTime;
}
