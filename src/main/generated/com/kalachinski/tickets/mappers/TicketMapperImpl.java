package com.kalachinski.tickets.mappers;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.dto.TicketDto;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-07-29T14:53:33+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 14.0.1 (Oracle Corporation)"
)
@Component
public class TicketMapperImpl implements TicketMapper {

    @Override
    public TicketDto convertToDto(Ticket entity) {
        if ( entity == null ) {
            return null;
        }

        TicketDto ticketDto = new TicketDto();

        ticketDto.setEventId( entityEventId( entity ) );
        ticketDto.setUserId( entityUserId( entity ) );
        ticketDto.setLocationId( entityLocationId( entity ) );
        ticketDto.setId( entity.getId() );
        ticketDto.setPlace( entity.getPlace() );
        ticketDto.setRow( entity.getRow() );
        ticketDto.setTicketStatus( entity.getTicketStatus() );

        return ticketDto;
    }

    @Override
    public Ticket convertToEntity(TicketDto entityDto) {
        if ( entityDto == null ) {
            return null;
        }

        Ticket ticket = new Ticket();

        ticket.setLocation( TicketMapper.convertLocationIdToLocation( entityDto.getLocationId() ) );
        ticket.setEvent( TicketMapper.convertEventIdToEvent( entityDto.getEventId() ) );
        ticket.setUser( TicketMapper.convertUserIdToUser( entityDto.getUserId() ) );
        ticket.setId( entityDto.getId() );
        ticket.setPlace( entityDto.getPlace() );
        ticket.setRow( entityDto.getRow() );
        ticket.setTicketStatus( entityDto.getTicketStatus() );

        return ticket;
    }

    private Long entityEventId(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }
        Event event = ticket.getEvent();
        if ( event == null ) {
            return null;
        }
        Long id = event.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityUserId(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }
        User user = ticket.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityLocationId(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }
        Location location = ticket.getLocation();
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
