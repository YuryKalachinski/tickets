package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.dto.EventDto;
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

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application_test.properties")
@Sql(value = {"/create_event_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete_event_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(authorities = "ADMIN")
class EventRestControllerIntegrationTest {

    @Value("${hostName}")
    private String hostName;

    private final MockMvc mockMvc;

    @Autowired
    EventRestControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Get one Event")
    void getOneEvent() throws Exception {
        mockMvc.perform(get("/event/1"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.name").value("Bi-2"),
                        jsonPath("$.dateTime").value("2020-06-10 18:00"),
                        jsonPath("$.locationId").value(1)
                ));
    }

    @Test
    @DisplayName("NotFound from Get one Event")
    void getOneEventWithNotFoundException() throws Exception {
        mockMvc.perform(get("/event/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get all Events by Location id")
    void getEventsByLocationId() throws Exception {
        mockMvc.perform(get("/event/?locationId=1"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$", hasSize(1)),
                        jsonPath("$.[0].id").value(1),
                        jsonPath("$.[0].name").value("Bi-2"),
                        jsonPath("$.[0].dateTime").value("2020-06-10 18:00"),
                        jsonPath("$.[0].locationId").value(1)));
        mockMvc.perform(get("/event/?locationId=2"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$", hasSize(0))
                ));
    }

    @Test
    @DisplayName("Get all Events")
    void getAllEvents() throws Exception {
        mockMvc.perform(get("/event"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$", hasSize(1)),
                        jsonPath("$.[0].id").value(1),
                        jsonPath("$.[0].name").value("Bi-2"),
                        jsonPath("$.[0].dateTime").value("2020-06-10 18:00"),
                        jsonPath("$.[0].locationId").value(1)
                ));
    }

    @Test
    @DisplayName("Save Event")
    void saveEvent() throws Exception {
        mockMvc.perform(post("/event")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .name("Nizkiz")
                        .dateTime(LocalDateTime.parse("2020-12-20T18:00:00"))
                        .locationId(1L)
                        .build())))
                .andExpect(matchAll(
                        header().string("Location", "http://" + hostName + "/event/2"),
                        status().isCreated()
                ));
    }

    @Test
    @DisplayName("BadRequest from Save Event")
    void saveEventWithBadRequestException() throws Exception {
        mockMvc.perform(post("/event")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new EventDto())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Event")
    void updateEvent() throws Exception {
        mockMvc.perform(put("/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .id(1L)
                        .name("Na-na")
                        .dateTime(LocalDateTime.now())
                        .locationId(1L)
                        .build())
                ))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("NotFound exception from Update Event")
    void updateEventWithNotFoundException() throws Exception {
        mockMvc.perform(put("/event/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .id(2L)
                        .name("Tutsi")
                        .dateTime(LocalDateTime.now())
                        .locationId(1L)
                        .build())
                ))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("BadRequest exception from Update Event")
    void updateEventWithBadRequestException() throws Exception {
        mockMvc.perform(put("/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new EventDto())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete Event")
    void deleteEvent() throws Exception {
        mockMvc.perform(delete("/event/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/event/100"))
                .andExpect(status().isNoContent());
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}