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

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application_test.properties")
@Sql(value = {"/delete_user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(authorities = "ADMIN")
class RegistrationRestControllerIntegrationTest {

    @Value("${hostName}")
    private String hostName;

    private final MockMvc mockMvc;

    @Autowired
    RegistrationRestControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Registration new User")
    void registrationUser() throws Exception {
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
                        .state(State.ACTIVE)
                        .build())))
                .andExpect(matchAll(
                        status().isCreated(),
                        header().string("Location", "http://" + hostName + "/checkIn/1")
                ));
    }

    @Test
    @DisplayName("BadRequest from Registration new User")
    void registrationUserWithBadRequestException() throws Exception {
        mockMvc.perform(post("/checkIn")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new UserDto())))
                .andExpect(status().isBadRequest());
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}