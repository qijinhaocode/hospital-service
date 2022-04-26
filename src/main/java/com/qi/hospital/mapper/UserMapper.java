package com.qi.hospital.mapper;

import com.qi.hospital.dto.user.UserRequest;
import com.qi.hospital.dto.user.UserResponse;
import com.qi.hospital.dto.user.UserUpdateRequest;
import com.qi.hospital.model.user.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE,builder = @Builder(disableBuilder = true))
public interface UserMapper {
    User toUser(UserRequest userRequest);

    @Mapping(source = "newPassword", target = "password")
    User toUser(UserUpdateRequest userUpdateRequest);

    List<UserResponse> toResponses(List<User> users);

    UserResponse toResponse(User user);
}

