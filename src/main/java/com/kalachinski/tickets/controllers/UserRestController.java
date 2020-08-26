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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Validated
public class UserRestController {

    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
//    @JsonView(ViewsDto.FullViews.class)
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Get all Users");
        return new ResponseEntity<>(userService.getAllUsers().stream()
                .map(userMapper::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
//    @JsonView(ViewsDto.FullViews.class)
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        log.info("Get User by id: {}", id);
        return new ResponseEntity<>(userMapper.convertToDto(
                userService.getUserById(id)), HttpStatus.OK);
    }

    @PostMapping
    @Validated(ViewsDto.New.class)
    public ResponseEntity<UserDto> saveUser(
            @RequestBody @Valid UserDto userDto) {
        log.info("Save new User: {}", userDto.toString());
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(userService.saveUser(
                        userMapper.convertToEntity(userDto)).getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Validated(ViewsDto.Exist.class)
    public ResponseEntity<UserDto> updateUser(
            @RequestBody @Valid UserDto userDto,
            @PathVariable("id") Long id) {
        log.info("Update User with id: {} with next body: {}", id, userDto.toString());
        userService.updateUser(userMapper.convertToEntity(userDto), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUserById(@PathVariable("id") Long id) {
        log.info("Delete User by id: {}", id);
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
