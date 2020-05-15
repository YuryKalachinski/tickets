package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.Location;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application_test.properties")
@Sql(value = {"/create_location_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete_location_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(authorities = "ADMIN")
class LocationRestControllerIntegrationTest {

    @Value("${hostName}")
    private String hostName;

    private final MockMvc mockMvc;

    @Autowired
    LocationRestControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Get one Location")
    void getOneLocation() throws Exception {
        mockMvc.perform(get("/location/1"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.name").value("Minsk-Arena"),
                        jsonPath("$.numberOfPlace").value(20),
                        jsonPath("$.numberOfRow").value(20)));
    }

    @Test
    @DisplayName("NotFound exception by Get one Location")
    void getOneLocationWithNotFoundException() throws Exception{
        mockMvc.perform(get("/location/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get all Locations")
    void getAllLocations( ) throws  Exception {
        mockMvc.perform(get("/location"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$", hasSize(1)),
                        jsonPath("$.[0].id").value(1),
                        jsonPath("$.[0].name").value("Minsk-Arena"),
                        jsonPath("$.[0].numberOfPlace").value(20),
                        jsonPath("$.[0].numberOfRow").value(20)));
    }

    @Test
    @DisplayName("Save Location")
    void saveLocation() throws Exception {
        mockMvc.perform(post("/location")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectAsJson(Location.builder()
                        .name("Dynamo Stadium")
                        .numberOfRow(100)
                        .numberOfPlace(100)
                        .build())))
                .andExpect(matchAll(
                        header().string("Location", "http://" + hostName + "/location/2"),
                        status().isCreated()));
    }

    @Test
    @DisplayName("BadRequest exception by Save Location")
    void saveLocationWithBadRequestException() throws Exception {
        mockMvc.perform(post("/location")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectAsJson(new Location())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Location")
    void updateLocation() throws Exception {
        mockMvc.perform(put("/location/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectAsJson(Location.builder()
                        .id(1L)
                        .name("Minsk-Arena")
                        .numberOfRow(100)
                        .numberOfPlace(100)
                        .build())))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("NotFound exception by Update Location")
    void updateLocationWithNotFoundException() throws Exception {
        mockMvc.perform(put("/location/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectAsJson(new Location())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("BadRequest exception by Update Location")
    void updateLocationWithBadRequestException() throws Exception {
        mockMvc.perform(put("/location/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectAsJson(new Location())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete Location")
    void deleteLocation() throws Exception {
        mockMvc.perform(delete("/location/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/location/100"))
                .andExpect(status().isNoContent());
    }

    private byte[] objectAsJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}