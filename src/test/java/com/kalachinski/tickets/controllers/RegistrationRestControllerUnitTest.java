package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.Role;
import com.kalachinski.tickets.domains.State;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.dto.UserDto;
import com.kalachinski.tickets.mappers.UserMapper;
import com.kalachinski.tickets.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({RegistrationRestController.class, UserMapper.class})
@WithMockUser(authorities = "ADMIN")
@TestPropertySource("/application_test.properties")
public class RegistrationRestControllerUnitTest {

    @Value("${hostName}")
    private String hostName;

    @MockBean
    private UserService userService;

    private final MockMvc mockMvc;

    @Autowired
    RegistrationRestControllerUnitTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Registration new User")
    void registrationUser() throws Exception {
        when(userService.registrationUser(any(User.class))).thenReturn(User.builder()
                .id(1L)
                .login("login")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .state(State.ACTIVE)
                .roles(Stream.of(Role.USER)
                        .collect(Collectors.toList()))
                .build());
        mockMvc.perform(post("/checkIn")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .login("login")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .state(State.ACTIVE)
                        .roles(Stream.of(Role.USER)
                                .collect(Collectors.toList()))
                        .build())))
                .andExpect(ResultMatcher.matchAll(
                        header().string("Location", "http://" + hostName + "/checkIn/1"),
                        status().isCreated()
                ));
        verify(userService, times(1)).registrationUser(any(User.class));
    }

    @Test
    @DisplayName("BadRequest from Registration new User without Login")
    void registrationUserWithBadRequestExceptionByLogin() throws Exception {
        mockMvc.perform(post("/checkIn")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .firstName("firstName")
                        .lastName("lastName")
                        .password("password")
                        .roles(Stream.of(Role.USER)
                                .collect(Collectors.toList()))
                        .state(State.ACTIVE)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(userService, never()).registrationUser(any(User.class));
    }

    @Test
    @DisplayName("BadRequest from Registration new User without FirstName")
    void registrationUserWithBadRequestExceptionByFirstName() throws Exception {
        mockMvc.perform(post("/checkIn")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .login("Login")
                        .lastName("lastName")
                        .password("password")
                        .roles(Stream.of(Role.USER)
                                .collect(Collectors.toList()))
                        .state(State.ACTIVE)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(userService, never()).registrationUser(any(User.class));
    }

    @Test
    @DisplayName("BadRequest from Registration new User without LastName")
    void registrationUserWithBadRequestExceptionByLastName() throws Exception {
        mockMvc.perform(post("/checkIn")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .login("Login")
                        .firstName("firstName")
                        .password("password")
                        .roles(Stream.of(Role.USER)
                                .collect(Collectors.toList()))
                        .state(State.ACTIVE)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(userService, never()).registrationUser(any(User.class));
    }

    @Test
    @DisplayName("BadRequest from Registration new User without Password")
    void registrationUserWithBadRequestExceptionByPassword() throws Exception {
        mockMvc.perform(post("/checkIn")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .login("Login")
                        .firstName("firstName")
                        .lastName("lastName")
                        .roles(Stream.of(Role.USER)
                                .collect(Collectors.toList()))
                        .state(State.ACTIVE)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(userService, never()).registrationUser(any(User.class));
    }

    @Test
    @DisplayName("BadRequest from Registration new User without UserRole")
    void registrationUserWithBadRequestExceptionByUserRole() throws Exception {
        mockMvc.perform(post("/checkIn")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .login("Login")
                        .firstName("firstName")
                        .lastName("lastName")
                        .password("password")
                        .state(State.ACTIVE)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(userService, never()).registrationUser(any(User.class));
    }

    @Test
    @DisplayName("BadRequest from Registration new User without UserState")
    void registrationUserWithBadRequestExceptionByUserState() throws Exception {
        mockMvc.perform(post("/checkIn")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .login("Login")
                        .firstName("firstName")
                        .lastName("lastName")
                        .password("password")
                        .roles(Stream.of(Role.USER)
                                .collect(Collectors.toList()))
                        .build())))
                .andExpect(status().isBadRequest());
        verify(userService, never()).registrationUser(any(User.class));
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}