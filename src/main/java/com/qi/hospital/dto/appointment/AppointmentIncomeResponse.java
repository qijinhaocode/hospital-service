package com.qi.hospital.dto.appointment;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AppointmentIncomeResponse {
    List<GetAppointmentResponse> appointmentResponses;
    Double income;
}
