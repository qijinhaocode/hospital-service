package com.qi.hospital.mapper;

import com.qi.hospital.dto.appointment.AppointmentRequest;
import com.qi.hospital.dto.appointment.AppointmentResponse;
import com.qi.hospital.dto.appointment.GetAppointmentResponse;
import com.qi.hospital.model.appointment.Appointment;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE, builder = @Builder(disableBuilder = true))
public interface AppointmentMapper {

    GetAppointmentResponse toGetResponse(Appointment appointment);

    Appointment toDomain(AppointmentRequest appointmentRequest);

    AppointmentResponse toResponse(Appointment appointment);

    List<GetAppointmentResponse> toGetResponse(List<Appointment> appointments);

}

