package com.qi.hospital.mapper;

import com.qi.hospital.dto.doctor.DoctorRequest;
import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.doctor.DoctorUpdateRequest;
import com.qi.hospital.model.dcotor.Doctor;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE,builder = @Builder(disableBuilder = true))
public interface DoctorMapper {
    Doctor toDoctor(DoctorRequest doctorRequest);

    Doctor toDoctor(DoctorUpdateRequest doctorRequest);

    DoctorResponse toDoctorResponse(Doctor doctor);
}

