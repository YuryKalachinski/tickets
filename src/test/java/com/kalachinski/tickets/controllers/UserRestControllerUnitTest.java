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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserRestController.class, UserMapper.class})
@WithMockUser(authorities = "ADMIN")
@TestPropertySource("/application_test.properties")
public class UserRestControllerUnitTest {

    @Value("${hostName}")
    private String hostName;

    @MockBean
    private UserService userService;

    private final MockMvc mockMvc;

    @Autowired
    UserRestControllerUnitTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Get all Users")
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(
                Stream.of(User.builder()
                        .id(1L)
                        .login("login")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .state(State.ACTIVE)
                        .roles(Stream.of(Role.ADMIN)
                                .collect(Collectors.toList()))
                        .build())
                        .collect(Collectors.toList()));
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Get User by ID")
    void getUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(User.builder()
                .id(1L)
                .login("login")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .state(State.ACTIVE)
                .roles(Stream.of(Role.ADMIN)
                        .collect(Collectors.toList()))
                .build());
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk());
        verify(userService, times(1)).getUserById(anyLong());
    }

    @Test
    @DisplayName("Save User")
    void saveUser() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(User.builder()
                .id(1L)
                .login("login")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .state(State.ACTIVE)
                .roles(Stream.of(Role.ADMIN)
                        .collect(Collectors.toList()))
                .build());
        mockMvc.perform(post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .login("login")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .state(State.ACTIVE)
                        .roles(Stream.of(Role.ADMIN)
                                .collect(Collectors.toList()))
                        .build())))
                .andExpect(ResultMatcher.matchAll(
                        header().string("Location", "http://" + hostName + "/user/1"),
                        status().isCreated()
                ));
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("BadRequest from Save User with ID")
    void saveUserWithBadRequestExceptionById() throws Exception {
        mockMvc.perform(post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .id(1L)
                        .login("login")
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
    @DisplayName("BadRequest from Save User without Login")
    void saveUserWithBadRequestExceptionByLogin() throws Exception {
        mockMvc.perform(post("/user")
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
    @DisplayName("BadRequest from Save User without FirstName")
    void saveUserWithBadRequestExceptionByFirstName() throws Exception {
        mockMvc.perform(post("/user")
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
    @DisplayName("BadRequest from Save User without LastName")
    void saveUserWithBadRequestExceptionByLastName() throws Exception {
        mockMvc.perform(post("/user")
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
    @DisplayName("BadRequest from Save User without Password")
    void saveUserWithBadRequestExceptionByPassword() throws Exception {
        mockMvc.perform(post("/user")
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
    @DisplayName("BadRequest from Save User without UserRole")
    void saveUserWithBadRequestExceptionByUserRole() throws Exception {
        mockMvc.perform(post("/user")
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
    @DisplayName("BadRequest from Save User without UserState")
    void saveUserWithBadRequestExceptionByUserState() throws Exception {
        mockMvc.perform(post("/user")
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

    @Test
    @DisplayName("Update User")
    void updateUser() throws Exception {
        mockMvc.perform(put("/user/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .login("login")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .state(State.ACTIVE)
                        .roles(Stream.of(Role.ADMIN)
                                .collect(Collectors.toList()))
                        .build())))
                .andExpect(status().isNoContent());
        mockMvc.perform(put("/user/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .id(1L)
                        .login("login")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .state(State.ACTIVE)
                        .roles(Stream.of(Role.ADMIN)
                                .collect(Collectors.toList()))
                        .build())))
                .andExpect(status().isNoContent());
        verify(userService, times(2)).updateUser(any(User.class), anyLong());
    }

    @Test
    @DisplayName("BadRequest from Update User without Login")
    void updateUserWithBadRequestExceptionByLogin() throws Exception {
        mockMvc.perform(put("/user/1")
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
    @DisplayName("BadRequest from Update User without FirstName")
    void updateUserWithBadRequestExceptionByFirstName() throws Exception {
        mockMvc.perform(put("/user/1")
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
    @DisplayName("BadRequest from Update User without LastName")
    void updateUserWithBadRequestExceptionByLastName() throws Exception {
        mockMvc.perform(put("/user/1")
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
    @DisplayName("BadRequest from Update User without Password")
    void updateUserWithBadRequestExceptionByPassword() throws Exception {
        mockMvc.perform(put("/user/1")
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
    @DisplayName("BadRequest from Update User without UserRole")
    void updateUserWithBadRequestExceptionByUserRole() throws Exception {
        mockMvc.perform(put("/user/1")
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
    @DisplayName("BadRequest from Update User without UserState")
    void updateUserWithBadRequestExceptionByUserState() throws Exception {
        mockMvc.perform(put("/user/1")
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

    @Test
    @DisplayName("Delete User")
    void deleteUserById() throws Exception {
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUserById(anyLong());
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}