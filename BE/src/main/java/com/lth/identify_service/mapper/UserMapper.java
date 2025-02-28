package com.lth.identify_service.mapper;

import com.lth.identify_service.dto.request.UserCreationRequest;
import com.lth.identify_service.dto.request.UserUpdateRequest;
import com.lth.identify_service.dto.response.UserResponse;
import com.lth.identify_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

//    @Mapping(source = "firstName", target = "lastName")
    UserResponse toUserResponse(User user);
}
