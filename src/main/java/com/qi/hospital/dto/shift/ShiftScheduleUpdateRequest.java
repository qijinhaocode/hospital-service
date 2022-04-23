package com.qi.hospital.dto.shift;

import com.qi.hospital.model.appointment.AppointmentTime;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ShiftScheduleUpdateRequest {
    @NotBlank
    private String id;
    private AppointmentTime appointmentTime;
    private Integer morningAppointmentCount;
    private Integer afternoonAppointmentCount;
}
