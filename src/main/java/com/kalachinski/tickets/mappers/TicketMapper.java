package com.kalachinski.tickets.mappers;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.dto.TicketDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TicketMapper extends BasicMapper<Ticket, TicketDto> {

    @Override
    @Mappings({
            @Mapping(target = "locationId", source = "location.id"),
            @Mapping(target = "eventId", source = "event.id"),
            @Mapping(target = "userId", source = "user.id")
    })
    TicketDto convertToDto(Ticket entity);

    @Override
    @Mappings({
            @Mapping(target = "location", source = "locationId", qualifiedByName = "convertLocationIdToLocation"),
            @Mapping(target = "event", source = "eventId", qualifiedByName = "convertEventIdToEvent"),
            @Mapping(target = "user", source = "userId", qualifiedByName = "convertUserIdToUser")
    })
    Ticket convertToEntity(TicketDto entityDto);

    @Named("convertLocationIdToLocation")
    static Location convertLocationIdToLocation(Long locationId) {
        return Location.builder()
                .id(locationId)
                .build();
    }

    @Named("convertEventIdToEvent")
    static Event convertEventIdToEvent(Long eventId) {
        return Event.builder()
                .id(eventId)
                .build();
    }

    @Named("convertUserIdToUser")
    static User convertUserIdToUser(Long userId) {
        return User.builder()
                .id(userId)
                .build();
    }
}
