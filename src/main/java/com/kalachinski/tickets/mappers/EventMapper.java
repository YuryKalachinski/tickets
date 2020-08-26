package com.kalachinski.tickets.mappers;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.dto.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EventMapper extends BasicMapper<Event, EventDto>{

    @Override
    @Mapping(target = "locationId", source = "location.id")
    EventDto convertToDto(Event event);

    @Override
    @Mappings({
            @Mapping(target = "location", source = "locationId", qualifiedByName = "convertLocationIdToLocation"),
            @Mapping(target = "tickets", ignore = true)
    })
    Event convertToEntity(EventDto entityDto);

    @Named("convertLocationIdToLocation")
    static Location convertLocationIdToLocation(Long locationId) {
        return Location.builder()
                .id(locationId)
                .build();
    }
}
