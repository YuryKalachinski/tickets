package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.domains.TicketStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application_test.properties")
@Sql(value = {"/create_ticket_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete_ticket_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(authorities = "ADMIN")
class TicketRestControllerIntegrationTest {

    private final MockMvc mockMvc;

    @Autowired
    TicketRestControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Get all Tickets")
    void getAllTickets() throws Exception {
        mockMvc.perform(get("/ticket"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$", hasSize(1)),
                        jsonPath("$.[0].id").value(1L),
                        jsonPath("$.[0].row").value(1L),
                        jsonPath("$.[0].place").value(1),
                        jsonPath("$.[0].ticketStatus").value("PURCHASED"),
                        jsonPath("$.[0].event.id").value(1),
                        jsonPath("$.[0].location.id").value(1)
                ));
    }

    @Test
    @DisplayName("Get one Ticket")
    void getTicket() throws Exception {
        mockMvc.perform(get("/ticket/1"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.row").value(1L),
                        jsonPath("$.place").value(1),
                        jsonPath("$.ticketStatus").value("PURCHASED"),
                        jsonPath("$.event.id").value(1),
                        jsonPath("$.location.id").value(1)
                ));
    }

    @Test
    @DisplayName("NotFound by Get one Ticket")
    void getOneTicketWithNotFountException() throws Exception {
        mockMvc.perform(get("/ticket/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get all Tickets by Event id")
    void getTicketsByEventId() throws Exception {
        mockMvc.perform(get("/ticket/?eventId=1"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$", hasSize(1)),
                        jsonPath("$.[0].id").value(1L),
                        jsonPath("$.[0].row").value(1L),
                        jsonPath("$.[0].place").value(1),
                        jsonPath("$.[0].ticketStatus").value("PURCHASED"),
                        jsonPath("$.[0].event.id").value(1),
                        jsonPath("$.[0].location.id").value(1)
                ));
        mockMvc.perform(get("/ticket/?eventId=2"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$", hasSize(0))
                ));
    }

    @Test
    @DisplayName("Get all Tickets by User id")
    void getTicketsByUserId() throws Exception {
        mockMvc.perform(get("/ticket/?userId=1"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$", hasSize(0))
                ));
    }

    @Test
    @DisplayName("Save group Tickets")
    void saveGroupTickets() throws Exception {
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(Ticket.builder()
                .place(1)
                .row(1)
                .ticketStatus(TicketStatus.PURCHASED)
                .event(Event.builder()
                        .id(1L)
                        .build())
                .location(Location.builder()
                        .id(1L)
                        .build())
                .build());
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(ticketList)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("BadRequest by Save group Tickets")
    void saveGroupTicketsWithBadRequestException() throws Exception {
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(Ticket.builder()
                .place(1)
                .row(1)
                .ticketStatus(TicketStatus.PURCHASED)
                .location(Location.builder()
                        .id(1L)
                        .build())
                .build());
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(ticketList)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Update Ticket")
    void updateTicket() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(Ticket.builder()
                        .id(2L)
                        .place(1)
                        .row(1)
                        .location(Location.builder()
                                .id(1L)
                                .build())
                        .ticketStatus(TicketStatus.PURCHASED)
                        .event(Event.builder()
                                .id(1L)
                                .build())
                        .build())
                ))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("NotFound exception by Update Ticket")
    void updateTicketWithNotFoundException() throws Exception {
        mockMvc.perform(put("/ticket/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new Ticket())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("BadRequest exception by Update Ticket")
    void updateTicketWithBadRequestException() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new Ticket())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete ticket")
    void deleteTicket() throws Exception {
        mockMvc.perform(delete("/ticket/1"))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/ticket/100"))
                .andExpect(status().isNoContent());
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsBytes(object);
    }
}