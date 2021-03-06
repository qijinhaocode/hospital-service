package com.qi.hospital.mapper;

import com.qi.hospital.dto.shift.ShiftScheduleResponse;
import com.qi.hospital.dto.shift.ShiftScheduleUpdateRequest;
import com.qi.hospital.model.shift.ShiftSchedule;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ShiftScheduleMapper {
    ShiftScheduleResponse toResponse(ShiftSchedule shiftSchedule);

    List<ShiftScheduleResponse> toResponses(List<ShiftSchedule> shiftSchedules);

    ShiftSchedule toDomain(ShiftScheduleUpdateRequest shiftScheduleUpdateRequest);
}

