package com.kalachinski.tickets.mappers;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.dto.EventDto;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-03T14:38:06+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.7 (JetBrains s.r.o.)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDto convertToDto(Event event) {
        if ( event == null ) {
            return null;
        }

        EventDto eventDto = new EventDto();

        eventDto.setLocationId( eventLocationId( event ) );
        eventDto.setId( event.getId() );
        eventDto.setName( event.getName() );
        eventDto.setDateTime( event.getDateTime() );

        return eventDto;
    }

    @Override
    public Event convertToEntity(EventDto entityDto) {
        if ( entityDto == null ) {
            return null;
        }

        Event event = new Event();

        event.setLocation( EventMapper.convertLocationIdToLocation( entityDto.getLocationId() ) );
        event.setId( entityDto.getId() );
        event.setName( entityDto.getName() );
        event.setDateTime( entityDto.getDateTime() );

        return event;
    }

    private Long eventLocationId(Event event) {
        if ( event == null ) {
            return null;
        }
        Location location = event.getLocation();
        if ( location == null ) {
            return null;
        }
        Long id = location.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
