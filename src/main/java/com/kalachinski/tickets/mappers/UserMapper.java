package com.kalachinski.tickets.mappers;

import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends BasicMapper<User, UserDto> {

    @Override
    @Mapping(target = "tickets", ignore = true)
    User convertToEntity(UserDto entityDto);
}
