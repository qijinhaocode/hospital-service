package com.qi.hospital.dto.appointment;

import com.qi.hospital.model.appointment.AppointmentStatus;
import com.qi.hospital.model.appointment.AppointmentTime;
import com.qi.hospital.model.dcotor.DoctorTitle;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private String userId;
    private String doctorJobNumber;
    private LocalDate localDate;
    private AppointmentTime appointmentTime;
    private AppointmentStatus appointmentStatus;
    private LocalDateTime payTime;

    private String doctorName;
    private DoctorTitle doctorTitle;
    private String doctorIntro;
    private String sectionName;
    private Double registrationFee;
}
