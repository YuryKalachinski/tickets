package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.Event;
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

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventRestController.class)
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
        when(eventService.getEventById(anyLong())).thenReturn(new Event());
        mockMvc.perform(get("/event/1"))
                .andExpect(status().isOk());
        verify(eventService, times(1)).getEventById(anyLong());
    }

    @Test
    @DisplayName("Get all Events by Location id")
    void getEventsByLocationId() throws Exception {
        when(eventService.getEventsByLocationId(anyLong())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/event/?locationId=1"))
                .andExpect(status().isOk());
        verify(eventService, times(1)).getEventsByLocationId(anyLong());
    }

    @Test
    @DisplayName("Get all Events")
    void getAllEvents() throws Exception {
        when(eventService.getAllEvents()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/event"))
                .andExpect(status().isOk());
        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    @DisplayName("Save Event")
    void saveEvent() throws Exception {
        when(eventService.saveEvent(any(Event.class))).thenReturn(Event.builder()
                .id(1L)
                .build());
        mockMvc.perform(post("/event")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new Event())))
                .andExpect(matchAll(
                        header().string("Location", "http://" + hostName + "/event/1"),
                        status().isCreated()
                ));
        verify(eventService, times(1)).saveEvent(any(Event.class));
    }

    @Test
    @DisplayName("Update Event")
    void updateEvent() throws Exception {
        mockMvc.perform(put("/event/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new Event())))
                .andExpect(status().isNoContent());
        verify(eventService, times(1)).updateEvent(any(Event.class), anyLong());
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