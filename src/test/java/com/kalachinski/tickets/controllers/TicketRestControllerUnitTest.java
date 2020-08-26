package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.*;
import com.kalachinski.tickets.dto.TicketDto;
import com.kalachinski.tickets.mappers.TicketMapper;
import com.kalachinski.tickets.services.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TicketRestController.class, TicketMapper.class})
@WithMockUser(authorities = "ADMIN")
class TicketRestControllerUnitTest {

    @MockBean
    private TicketService ticketService;

    private final MockMvc mockMvc;

    @Autowired
    TicketRestControllerUnitTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Get all Tickets")
    void getAllTickets() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(
                Stream.of(Ticket.builder()
                        .id(1L)
                        .place(1)
                        .row(1)
                        .event(Event.builder().id(1L).build())
                        .location(Location.builder().id(1L).build())
                        .user(User.builder().id(1L).build())
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build())
                        .collect(Collectors.toList()));
        mockMvc.perform(get("/ticket"))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).getAllTickets();
    }

    @Test
    @DisplayName("Get one Ticket")
    void getTicket() throws Exception {
        when(ticketService.getTicketById(anyLong())).thenReturn(
                Ticket.builder()
                        .id(1L)
                        .place(1)
                        .row(1)
                        .event(Event.builder().id(1L).build())
                        .location(Location.builder().id(1L).build())
                        .user(User.builder().id(1L).build())
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build());
        mockMvc.perform(get("/ticket/1"))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).getTicketById(anyLong());
    }

    @Test
    @DisplayName("Get all Tickets by Event id")
    void getTicketsByEventId() throws Exception {
        when(ticketService.getTicketsByEventId(anyLong())).thenReturn(
                Stream.of(Ticket.builder()
                        .id(1L)
                        .place(1)
                        .row(1)
                        .event(Event.builder().id(1L).build())
                        .location(Location.builder().id(1L).build())
                        .user(User.builder().id(1L).build())
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build())
                        .collect(Collectors.toList()));
        mockMvc.perform(get("/ticket/?eventId=1"))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).getTicketsByEventId(anyLong());
    }

    @Test
    @DisplayName("Get all Tickets by User id")
    void getTicketsByUserId() throws Exception {
        when(ticketService.getTicketsByUserId(anyLong())).thenReturn(
                Stream.of(Ticket.builder()
                        .id(1L)
                        .place(1)
                        .row(1)
                        .event(Event.builder().id(1L).build())
                        .location(Location.builder().id(1L).build())
                        .user(User.builder().id(1L).build())
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build())
                        .collect(Collectors.toList()));
        mockMvc.perform(get("/ticket/?userId=1"))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).getTicketsByUserId(anyLong());
    }

    @Test
    @DisplayName("Save group Tickets")
    void saveGroupTickets() throws Exception {
        when(ticketService.saveGroupTickets(any())).thenReturn(
                Stream.of(Ticket.builder()
                        .id(1L)
                        .place(1)
                        .row(1)
                        .event(Event.builder().id(1L).build())
                        .location(Location.builder().id(1L).build())
                        .user(User.builder().id(1L).build())
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build()).collect(Collectors.toList()));
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .eventId(1L)
                                .locationId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isCreated());
        verify(ticketService, times(1)).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Save group Tickets with ID")
    void saveGroupTicketsWithBadRequestExceptionById() throws Exception {
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .id(1L)
                                .place(1)
                                .row(1)
                                .eventId(1L)
                                .locationId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Save group Tickets without place")
    void saveGroupTicketsWithBadRequestExceptionByPlace() throws Exception {
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .row(1)
                                .eventId(1L)
                                .locationId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Save group Tickets without row")
    void saveGroupTicketsWithBadRequestExceptionByRow() throws Exception {
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .eventId(1L)
                                .locationId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Save group Tickets without EventId")
    void saveGroupTicketsWithBadRequestExceptionByEventId() throws Exception {
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .locationId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Save group Tickets without LocationId")
    void saveGroupTicketsWithBadRequestExceptionByLocationId() throws Exception {
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .eventId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Save group Tickets without UserId")
    void saveGroupTicketsWithBadRequestExceptionByUserID() throws Exception {
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .eventId(1L)
                                .locationId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Save group Tickets without Ticket Status")
    void saveGroupTicketsWithBadRequestExceptionByTicketStatus() throws Exception {
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .eventId(1L)
                                .locationId(1L)
                                .userId(1L)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("Update Ticket")
    void updateTicket() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(TicketDto.builder()
                        .place(1)
                        .row(1)
                        .eventId(1L)
                        .locationId(1L)
                        .userId(1L)
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build())))
                .andExpect(status().isNoContent());
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(TicketDto.builder()
                        .id(1L)
                        .place(1)
                        .row(1)
                        .eventId(1L)
                        .locationId(1L)
                        .userId(1L)
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build())))
                .andExpect(status().isNoContent());
        verify(ticketService, times(2)).updateTicket(any(Ticket.class), anyLong());
    }

    @Test
    @DisplayName("BadRequest from Update group Tickets without place")
    void updateGroupTicketsWithBadRequestExceptionByPlace() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .row(1)
                                .eventId(1L)
                                .locationId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Update group Tickets without row")
    void updateGroupTicketsWithBadRequestExceptionByRow() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .eventId(1L)
                                .locationId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Update group Tickets without EventId")
    void updateGroupTicketsWithBadRequestExceptionByEventId() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .locationId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Update group Tickets without LocationId")
    void updateGroupTicketsWithBadRequestExceptionByLocationId() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .eventId(1L)
                                .userId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Update group Tickets without UserId")
    void updateGroupTicketsWithBadRequestExceptionByUserID() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .eventId(1L)
                                .locationId(1L)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("BadRequest from Update group Tickets without Ticket Status")
    void updateGroupTicketsWithBadRequestExceptionByTicketStatus() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .eventId(1L)
                                .locationId(1L)
                                .userId(1L)
                                .build()).collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());
        verify(ticketService, never()).saveGroupTickets(any());
    }

    @Test
    @DisplayName("Delete Ticket")
    void deleteTicket() throws Exception {
        mockMvc.perform(delete("/ticket/1"))
                .andExpect(status().isNoContent());
        verify(ticketService, times(1)).deleteTicket(anyLong());
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}