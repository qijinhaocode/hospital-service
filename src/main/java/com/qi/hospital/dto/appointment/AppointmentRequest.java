package com.qi.hospital.dto.appointment;

import com.qi.hospital.model.appointment.AppointmentStatus;
import com.qi.hospital.model.appointment.AppointmentTime;
import com.qi.hospital.model.dcotor.DoctorTitle;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    //可以不传，通过token 中的手机号获取
    private String userId;
    @NotBlank
    private String doctorJobNumber;
    @NotNull
    private LocalDate localDate;
    @NotNull
    private AppointmentTime appointmentTime;
    private AppointmentStatus appointmentStatus;
    @NotNull
    private LocalDateTime payTime;
    @NotNull
    private String doctorName;
    @NotNull
    private DoctorTitle doctorTitle;
    @NotNull
    private String doctorIntro;
    @NotNull
    private String sectionName;
    @NotNull
    private Double registrationFee;
}
