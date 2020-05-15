package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.services.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketRestController.class)
@WithMockUser(authorities = "ADMIN")
@TestPropertySource("/application_test.properties")
class TicketRestControllerUnitTest {

    private final MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    TicketRestControllerUnitTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Get all Tickets")
    void getAllTickets() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/ticket"))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).getAllTickets();
    }

    @Test
    @DisplayName("Get one Ticket")
    void getTicket() throws Exception {
        when(ticketService.getTicketById(anyLong())).thenReturn(new Ticket());
        mockMvc.perform(get("/ticket/1"))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).getTicketById(anyLong());
    }

    @Test
    @DisplayName("Get all Tickets by Event id")
    void getTicketsByEventId() throws Exception {
        when(ticketService.getTicketsByEventId(anyLong())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/ticket/?eventId=1"))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).getTicketsByEventId(anyLong());
    }

    @Test
    @DisplayName("Get all Tickets by User id")
    void getTicketsByUserId() throws Exception {
        when(ticketService.getTicketsByUserId(anyLong())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/ticket/?userId=1"))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).getTicketsByUserId(anyLong());
    }

    @Test
    @DisplayName("Save group Tickets")
    void saveGroupTickets() throws Exception {
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket());
        when(ticketService.saveGroupTickets(any())).thenReturn(new ArrayList<>());
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(ticketList)))
                .andExpect(status().isCreated());
        verify(ticketService, times(1)).saveGroupTickets(any());
    }

    @Test
    @DisplayName("Update Ticket")
    void updateTicket() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new Ticket())))
                .andExpect(status().isNoContent());
        verify(ticketService, times(1)).updateTicket(any(Ticket.class), anyLong());
    }

    @Test
    void deleteTicket() throws Exception {
        mockMvc.perform(delete("/ticket/1"))
                .andExpect(status().isNoContent());
        verify(ticketService, times(1)).deleteTicket(anyLong());
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}