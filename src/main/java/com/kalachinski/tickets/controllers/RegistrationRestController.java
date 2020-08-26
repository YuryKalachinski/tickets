package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.dto.UserDto;
import com.kalachinski.tickets.dto.ViewsDto;
import com.kalachinski.tickets.mappers.UserMapper;
import com.kalachinski.tickets.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/checkIn")
@Validated
public class RegistrationRestController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationRestController.class);

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public RegistrationRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @Validated(ViewsDto.New.class)
    public ResponseEntity<UserDto> registrationUser(
            @RequestBody @Valid UserDto userDto) {
        log.info("Save new User: {}", userDto.toString());
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(userService.registrationUser(userMapper.convertToEntity(userDto)).getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
