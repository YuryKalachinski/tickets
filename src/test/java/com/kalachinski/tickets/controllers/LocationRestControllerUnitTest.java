package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.dto.LocationDto;
import com.kalachinski.tickets.mappers.LocationMapper;
import com.kalachinski.tickets.services.LocationService;
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({LocationRestController.class, LocationMapper.class})
@WithMockUser(authorities = "ADMIN")
@TestPropertySource("/application_test.properties")
class LocationRestControllerUnitTest {

    @Value("${hostName}")
    private String hostName;

    @MockBean
    private LocationService locationService;

    private final MockMvc mockMvc;

    @Autowired
    LocationRestControllerUnitTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Get one Location")
    void getOneLocation() throws Exception {
        when(locationService.getLocationById(anyLong())).thenReturn(
                Location.builder()
                        .id(1L)
                        .name("Dynamo-stadium")
                        .numberOfPlace(100)
                        .numberOfRow(100)
                        .build());
        mockMvc.perform(get("/location/1"))
                .andExpect(status().isOk());
        verify(locationService, times(1)).getLocationById(anyLong());
    }

    @Test
    @DisplayName("Get all Locations")
    void getAllLocation() throws Exception {
        when(locationService.getAllLocations()).thenReturn(
                Stream.of(Location.builder()
                        .id(1L)
                        .name("Dynamo-stadium")
                        .numberOfPlace(100)
                        .numberOfRow(100)
                        .build())
                        .collect(Collectors.toList()));
        mockMvc.perform(get("/location"))
                .andExpect(status().isOk());
        verify(locationService, times(1)).getAllLocations();
    }

    @Test
    @DisplayName("Save Location")
    void saveLocation() throws Exception {
        when(locationService.saveLocation(any(Location.class))).thenReturn(
                Location.builder()
                        .id(1L)
                        .name("Dynamo-stadium")
                        .numberOfPlace(100)
                        .numberOfRow(100)
                        .build());
        mockMvc.perform(post("/location")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .name("Dynamo-stadium")
                        .numberOfRow(100)
                        .numberOfPlace(100)
                        .build())))
                .andExpect(matchAll(
                        header().string("Location", "http://" + hostName + "/location/1"),
                        status().isCreated()
                ));
        verify(locationService, times(1)).saveLocation(any(Location.class));
    }

    @Test
    @DisplayName("BadRequest from Save Location with ID")
    void saveLocationWithBadRequestExceptionById() throws Exception {
        mockMvc.perform(post("/location")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .id(1L)
                        .name("Dynamo-stadium")
                        .numberOfRow(100)
                        .numberOfPlace(100)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(locationService, never()).saveLocation(any(Location.class));
    }

    @Test
    @DisplayName("BadRequest from Save Location without Name")
    void saveLocationWithBadRequestExceptionByName() throws Exception {
        mockMvc.perform(post("/location")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .numberOfRow(100)
                        .numberOfPlace(100)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(locationService, never()).saveLocation(any(Location.class));
    }

    @Test
    @DisplayName("BadRequest from Save Location without NumberOfRow")
    void saveLocationWithBadRequestExceptionByNumberOfRow() throws Exception {
        mockMvc.perform(post("/location")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .name("Dynamo-stadium")
                        .numberOfPlace(100)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(locationService, never()).saveLocation(any(Location.class));
    }

    @Test
    @DisplayName("BadRequest from Save Location without NumberOfPlace")
    void saveLocationWithBadRequestExceptionByNumberOfPlace() throws Exception {
        mockMvc.perform(post("/location")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .name("Dynamo-stadium")
                        .numberOfRow(100)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(locationService, never()).saveLocation(any(Location.class));
    }

    @Test
    @DisplayName("Update Location")
    void updateLocation() throws Exception {
        mockMvc.perform(put("/location/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .name("Dynamo-stadium")
                        .numberOfRow(100)
                        .numberOfPlace(100)
                        .build())))
                .andExpect(status().isNoContent());
        mockMvc.perform(put("/location/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .id(1L)
                        .name("Dynamo-stadium")
                        .numberOfRow(100)
                        .numberOfPlace(100)
                        .build())))
                .andExpect(status().isNoContent());
        verify(locationService, times(2)).updateLocation(any(Location.class), anyLong());
    }

    @Test
    @DisplayName("BadRequest from Update Location without Name")
    void updateLocationWithBadRequestExceptionByName() throws Exception {
        mockMvc.perform(put("/location/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .numberOfRow(100)
                        .numberOfPlace(100)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(locationService, never()).saveLocation(any(Location.class));
    }

    @Test
    @DisplayName("BadRequest from Update Location without NumberOfRow")
    void updateLocationWithBadRequestExceptionByNumberOfRow() throws Exception {
        mockMvc.perform(put("/location/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .name("Name")
                        .numberOfPlace(100)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(locationService, never()).saveLocation(any(Location.class));
    }

    @Test
    @DisplayName("BadRequest from Update Location without NumberOfPlace")
    void updateLocationWithBadRequestExceptionByNumberOfPlace() throws Exception {
        mockMvc.perform(put("/location/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(LocationDto.builder()
                        .name("Name")
                        .numberOfRow(100)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(locationService, never()).saveLocation(any(Location.class));
    }

    @Test
    @DisplayName("Delete Location")
    void deleteLocation() throws Exception {
        mockMvc.perform(delete("/location/1"))
                .andExpect(status().isNoContent());
        verify(locationService, times(1)).deleteLocation(anyLong());
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}