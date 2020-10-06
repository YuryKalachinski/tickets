package com.kalachinski.tickets.mappers;

import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.dto.LocationDto;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-03T14:38:06+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.7 (JetBrains s.r.o.)"
)
@Component
public class LocationMapperImpl implements LocationMapper {

    @Override
    public LocationDto convertToDto(Location entity) {
        if ( entity == null ) {
            return null;
        }

        LocationDto locationDto = new LocationDto();

        locationDto.setId( entity.getId() );
        locationDto.setName( entity.getName() );
        locationDto.setNumberOfPlace( entity.getNumberOfPlace() );
        locationDto.setNumberOfRow( entity.getNumberOfRow() );

        return locationDto;
    }

    @Override
    public Location convertToEntity(LocationDto entityDto) {
        if ( entityDto == null ) {
            return null;
        }

        Location location = new Location();

        location.setId( entityDto.getId() );
        location.setName( entityDto.getName() );
        location.setNumberOfPlace( entityDto.getNumberOfPlace() );
        location.setNumberOfRow( entityDto.getNumberOfRow() );

        return location;
    }
}
