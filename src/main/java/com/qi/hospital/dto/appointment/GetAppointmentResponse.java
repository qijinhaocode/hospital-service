package com.qi.hospital.dto.appointment;

import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.model.appointment.AppointmentStatus;
import com.qi.hospital.model.appointment.AppointmentTime;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GetAppointmentResponse {
    private String userId;
    private DoctorResponse doctorResponse;
    private LocalDate localDate;
    private AppointmentTime appointmentTime;
    private AppointmentStatus appointmentStatus;
    private LocalDateTime payTime;
}
