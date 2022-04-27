package com.qi.hospital.controller;

import com.qi.hospital.dto.appointment.AppointmentIncomeResponse;
import com.qi.hospital.dto.appointment.AppointmentRequest;
import com.qi.hospital.dto.appointment.AppointmentResponse;
import com.qi.hospital.dto.appointment.AppointmentUpdateRequest;
import com.qi.hospital.dto.appointment.GetAppointmentResponse;
import com.qi.hospital.model.appointment.AppointmentStatus;
import com.qi.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
@Validated
@CrossOrigin(origins = "*")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse createAppointment(@NotBlank @RequestHeader String token, @RequestBody @Valid AppointmentRequest appointmentRequest) {
        return appointmentService.createAppointment(token, appointmentRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAppointment(@NotBlank @RequestHeader("token") String token, @NotBlank @RequestParam("doctorJobNumber") String doctorJobNumber,
                                  @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("localdate") LocalDate localDate) {
        appointmentService.deleteAppointment(token, doctorJobNumber, localDate);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse updateAppointment(@NotBlank @RequestHeader("token") String token, @RequestBody @Valid AppointmentUpdateRequest appointmentUpdateRequest) {
        return appointmentService.updateAppointment(token, appointmentUpdateRequest);
    }

    //user get all appointment record
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GetAppointmentResponse> getAllAppointment(@NotBlank @RequestHeader("token") String token) {
        return appointmentService.getAllAppointment(token);
    }

    @GetMapping("income")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentIncomeResponse getIncomeByDate(@NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("startDate") LocalDate startDate,
                                                     @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("endDate") LocalDate endDate,
                                                     @RequestParam(required = false, defaultValue = "UNKNOWN") AppointmentStatus appointmentStatus) {
        return appointmentService.getIncomeByDate(startDate, endDate, appointmentStatus);
    }
}
