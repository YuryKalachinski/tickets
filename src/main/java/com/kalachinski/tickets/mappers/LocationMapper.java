package com.kalachinski.tickets.mappers;

import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.dto.LocationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface LocationMapper extends BasicMapper<Location, LocationDto> {

    @Override
    @Mappings({
            @Mapping(target = "events", ignore = true),
            @Mapping(target = "tickets", ignore = true)
    })
    Location convertToEntity(LocationDto entityDto);
}
