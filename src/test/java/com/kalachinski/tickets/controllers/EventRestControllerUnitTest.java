package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.dto.EventDto;
import com.kalachinski.tickets.mappers.EventMapper;
import com.kalachinski.tickets.services.EventService;
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

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({EventRestController.class, EventMapper.class})
@WithMockUser(authorities = "ADMIN")
@TestPropertySource("/application_test.properties")
class EventRestControllerUnitTest {

    @Value("${hostName}")
    private String hostName;

    private final MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    EventRestControllerUnitTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Get one Event")
    void getOneEvent() throws Exception {
        when(eventService.getEventById(anyLong())).thenReturn(Event.builder()
                .id(1L)
                .name("Bi-2")
                .dateTime(LocalDateTime.now())
                .location(Location.builder().id(1L).build())
                .build());
        mockMvc.perform(get("/event/1"))
                .andExpect(status().isOk());
        verify(eventService, times(1)).getEventById(anyLong());
    }

    @Test
    @DisplayName("Get all Events by Location id")
    void getEventsByLocationId() throws Exception {
        when(eventService.getEventsByLocationId(anyLong())).thenReturn(
                Stream.of(Event.builder()
                        .id(1L)
                        .name("Bi-2")
                        .dateTime(LocalDateTime.now())
                        .location(Location.builder().id(1L).build())
                        .build())
                        .collect(Collectors.toList()));
        mockMvc.perform(get("/event/?locationId=1"))
                .andExpect(status().isOk());
        verify(eventService, times(1)).getEventsByLocationId(anyLong());
    }

    @Test
    @DisplayName("Get all Events")
    void getAllEvents() throws Exception {
        when(eventService.getAllEvents()).thenReturn(
                Stream.of(Event.builder()
                        .id(1L)
                        .name("Bi-2")
                        .dateTime(LocalDateTime.now())
                        .location(Location.builder().id(1L).build())
                        .build())
                        .collect(Collectors.toList()));
        mockMvc.perform(get("/event"))
                .andExpect(status().isOk());
        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    @DisplayName("Save Event")
    void saveEvent() throws Exception {
        when(eventService.saveEvent(any(Event.class))).thenReturn(
                Event.builder()
                        .id(1L)
                        .name("Bi-2")
                        .dateTime(LocalDateTime.now())
                        .location(Location.builder().id(1L).build())
                        .build());
        mockMvc.perform(post("/event")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .name("Bi-2")
                        .dateTime(LocalDateTime.now())
                        .locationId(1L)
                        .build())))
                .andExpect(matchAll(
                        header().string("Location", "http://" + hostName + "/event/1"),
                        status().isCreated()
                ));
        verify(eventService, times(1)).saveEvent(any(Event.class));
    }

    @Test
    @DisplayName("BadRequest from Save Event with ID")
    void saveEventWithBadRequestExceptionById() throws Exception {
        mockMvc.perform(post("/event")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .id(1L)
                        .name("Bi-2")
                        .dateTime(LocalDateTime.now())
                        .locationId(1L)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(eventService, never()).saveEvent(any(Event.class));
    }

    @Test
    @DisplayName("BadRequest from Save Event without Name")
    void saveEventWithBadRequestExceptionByName() throws Exception {
        mockMvc.perform(post("/event")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .dateTime(LocalDateTime.now())
                        .locationId(1L)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(eventService, never()).saveEvent(any(Event.class));
    }

    @Test
    @DisplayName("BadRequest from Save Event without DateTime")
    void saveEventWithBadRequestExceptionByDateTime() throws Exception {
        mockMvc.perform(post("/event")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .name("Bi-2")
                        .locationId(1L)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(eventService, never()).saveEvent(any(Event.class));
    }

    @Test
    @DisplayName("BadRequest from Save Event without LocationId")
    void saveEventWithBadRequestExceptionByLocationId() throws Exception {
        mockMvc.perform(post("/event")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .name("Bi-2")
                        .dateTime(LocalDateTime.now())
                        .build())))
                .andExpect(status().isBadRequest());
        verify(eventService, never()).saveEvent(any(Event.class));
    }

    @Test
    @DisplayName("Update Event")
    void updateEvent() throws Exception {
        mockMvc.perform(put("/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .name("DDT")
                        .dateTime(LocalDateTime.now())
                        .locationId(1L)
                        .build())))
                .andExpect(status().isNoContent());
        mockMvc.perform(put("/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .id(1L)
                        .name("DDT")
                        .dateTime(LocalDateTime.now())
                        .locationId(1L)
                        .build())))
                .andExpect(status().isNoContent());
        verify(eventService, times(2)).updateEvent(any(Event.class), anyLong());
    }

    @Test
    @DisplayName("BadRequest from Update Event without Name")
    void updateEventWithBadRequestExceptionByName() throws Exception {
        mockMvc.perform(put("/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .dateTime(LocalDateTime.now())
                        .locationId(1L)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(eventService, never()).updateEvent(any(Event.class), anyLong());
    }

    @Test
    @DisplayName("BadRequest from Update Event without DateTime")
    void updateEventWithBadRequestExceptionByDateTime() throws Exception {
        mockMvc.perform(put("/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .name("DDT")
                        .locationId(1L)
                        .build())))
                .andExpect(status().isBadRequest());
        verify(eventService, never()).updateEvent(any(Event.class), anyLong());
    }

    @Test
    @DisplayName("BadRequest from Update Event without LocationId")
    void updateEventWithBadRequestExceptionByLocationId() throws Exception {
        mockMvc.perform(put("/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(EventDto.builder()
                        .name("DDT")
                        .dateTime(LocalDateTime.now())
                        .build())))
                .andExpect(status().isBadRequest());
        verify(eventService, never()).updateEvent(any(Event.class), anyLong());
    }

    @Test
    @DisplayName("Delete Event")
    void deleteEvent() throws Exception {
        mockMvc.perform(delete("/event/1"))
                .andExpect(status().isNoContent());
        verify(eventService, times(1)).deleteEvent(anyLong());
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}