package com.qi.hospital.mapper;

import com.qi.hospital.dto.doctor.DoctorRequest;
import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.doctor.DoctorUpdateRequest;
import com.qi.hospital.dto.shift.ShiftRequest;
import com.qi.hospital.dto.shift.ShiftResponse;
import com.qi.hospital.dto.shift.ShiftUpdateRequest;
import com.qi.hospital.model.dcotor.Doctor;
import com.qi.hospital.model.shift.Shift;
import com.qi.hospital.service.ShiftService;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE,builder = @Builder(disableBuilder = true))
public interface ShiftMapper {

 Shift toShift(ShiftRequest shiftRequest);

 Shift toShift(ShiftUpdateRequest shiftUpdateRequest);

 ShiftResponse toResponse(Shift shift);

}

