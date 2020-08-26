package com.kalachinski.tickets.mappers;

import com.kalachinski.tickets.domains.Role;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.dto.UserDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-07-29T14:53:33+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 14.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto convertToDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( entity.getId() );
        List<Role> list = entity.getRoles();
        if ( list != null ) {
            userDto.setRoles( new ArrayList<Role>( list ) );
        }
        userDto.setCreationDate( entity.getCreationDate() );
        userDto.setLogin( entity.getLogin() );
        userDto.setPassword( entity.getPassword() );
        userDto.setConfirmPassword( entity.getConfirmPassword() );
        userDto.setFirstName( entity.getFirstName() );
        userDto.setLastName( entity.getLastName() );
        userDto.setState( entity.getState() );

        return userDto;
    }

    @Override
    public User convertToEntity(UserDto entityDto) {
        if ( entityDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( entityDto.getId() );
        List<Role> list = entityDto.getRoles();
        if ( list != null ) {
            user.setRoles( new ArrayList<Role>( list ) );
        }
        user.setCreationDate( entityDto.getCreationDate() );
        user.setLogin( entityDto.getLogin() );
        user.setPassword( entityDto.getPassword() );
        user.setConfirmPassword( entityDto.getConfirmPassword() );
        user.setFirstName( entityDto.getFirstName() );
        user.setLastName( entityDto.getLastName() );
        user.setState( entityDto.getState() );

        return user;
    }
}
