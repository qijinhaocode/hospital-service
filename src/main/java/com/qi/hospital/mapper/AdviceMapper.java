package com.qi.hospital.mapper;

import com.qi.hospital.dto.advice.AdviceRequest;
import com.qi.hospital.dto.section.SectionRequest;
import com.qi.hospital.model.advice.Advice;
import com.qi.hospital.model.section.Section;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE,builder = @Builder(disableBuilder = true))
public interface AdviceMapper {

    @Mapping(target = "createDateTime", source = "createDateTime")
    Advice toDomain(AdviceRequest adviceRequest);

}

