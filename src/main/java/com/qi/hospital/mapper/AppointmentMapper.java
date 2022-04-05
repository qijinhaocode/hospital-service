package com.qi.hospital.mapper;

import com.qi.hospital.dto.appointment.AppointmentRequest;
import com.qi.hospital.dto.appointment.AppointmentResponse;
import com.qi.hospital.model.appointment.Appointment;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE, builder = @Builder(disableBuilder = true))
public interface AppointmentMapper {

    Appointment toDomain(AppointmentRequest appointmentRequest);

    AppointmentResponse toResponse(Appointment appointment);

}

