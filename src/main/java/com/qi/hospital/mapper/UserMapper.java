package com.qi.hospital.mapper;

import com.qi.hospital.dto.user.UserRequest;
import com.qi.hospital.dto.user.UserResponse;
import com.qi.hospital.model.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE,builder = @Builder(disableBuilder = true))
public interface UserMapper {

    User toUser(UserRequest userRequest);

    List<UserResponse> toResponses(List<User> users);

}

