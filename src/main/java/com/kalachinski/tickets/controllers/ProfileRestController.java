package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.dto.UserDto;
import com.kalachinski.tickets.mappers.UserMapper;
import com.kalachinski.tickets.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/user")
public class ProfileRestController {
    Logger log = LoggerFactory.getLogger(ProfileRestController.class);

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public ProfileRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<UserDto> getAuthUser(@AuthenticationPrincipal UserDetails user) {

        log.info("Get Authorization user");
        return new ResponseEntity<>(userMapper.convertToDto(userService.getUserByLogin(user.getUsername())),
                HttpStatus.OK);
    }
}
