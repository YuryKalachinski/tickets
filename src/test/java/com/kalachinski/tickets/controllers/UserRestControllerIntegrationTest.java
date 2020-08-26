package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.Role;
import com.kalachinski.tickets.domains.State;
import com.kalachinski.tickets.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application_test.properties")
@Sql(value = {"/create_user_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete_user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(authorities = "ADMIN")
class UserRestControllerIntegrationTest {

    @Value("${hostName}")
    private String hostName;

    private final MockMvc mockMvc;

    @Autowired
    UserRestControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Get all Users")
    void getAllUsers() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$", hasSize(1)),
                        jsonPath("$.[0].id").value(1L),
                        jsonPath("$.[0].login").value("login"),
                        jsonPath("$.[0].password").value("password"),
                        jsonPath("$.[0].firstName").value("firstName"),
                        jsonPath("$.[0].lastName").value("lastName"),
                        jsonPath("$.[0].state").value("ACTIVE"),
                        jsonPath("$.[0].roles[0]").value("ADMIN")
                ));
    }

    @Test
    @DisplayName("Get User by ID")
    void getUserById() throws Exception {
        mockMvc.perform(get("/user/1"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.login").value("login"),
                        jsonPath("$.password").value("password"),
                        jsonPath("$.firstName").value("firstName"),
                        jsonPath("$.lastName").value("lastName"),
                        jsonPath("$.state").value("ACTIVE"),
                        jsonPath("$.roles[0]").value("ADMIN")
                ));
    }

    @Test
    @DisplayName("NotFoundException from Get User by ID")
    void getUserByIdWithNotFoundException() throws Exception {
        mockMvc.perform(get("/user/2"))
                .andExpect(matchAll(
                        status().isNotFound()
                ));
    }

    @Test
    @DisplayName("Save User")
    void saveUser() throws Exception {
        mockMvc.perform(post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(UserDto.builder()
                        .login("Login")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .roles(Stream.of(Role.ADMIN)
                                .collect(Collectors.toList()))
                        .state(State.ACTIVE)
                        .build())))
                .andExpect(matchAll(
                        status().isCreated(),
                        header().string("Location", "http://" + hostName + "/user/2")
                ));
    }

    @Test
    @DisplayName("BadRequest from Save User")
    void saveUserWithBadRequestException() throws Exception {
        mockMvc.perform(post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new UserDto())))
                .andExpect(status().isBadRequest());
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
                        .login("login")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .state(State.ACTIVE)
                        .roles(Stream.of(Role.ADMIN)
                                .collect(Collectors.toList()))
                        .build())))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("NotFoundException from Update User")
    void updateUserWithNotFoundException() throws Exception {
        mockMvc.perform(put("/user/2")
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
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("BadRequest exception from Update User")
    void updateUserWithBAdRequestException() throws Exception {
        mockMvc.perform(put("/user/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new UserDto())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete User")
    void deleteUserById() throws Exception {
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/user/10"))
                .andExpect(status().isNoContent());
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}