package com.qi.hospital.mapper;

import com.qi.hospital.dto.registrationFee.RegistrationFeeUpdateRequest;
import com.qi.hospital.model.registrationFee.RegistrationFee;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE,builder = @Builder(disableBuilder = true))
public interface RegistrationFeeMapper {
    RegistrationFee toDomain (RegistrationFeeUpdateRequest updateRegistrationFeeRequest);
}
