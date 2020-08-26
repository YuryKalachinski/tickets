package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.domains.TicketStatus;
import com.kalachinski.tickets.dto.TicketDto;
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                        jsonPath("$.[0].eventId").value(1),
                        jsonPath("$.[0].locationId").value(1)
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
                        jsonPath("$.eventId").value(1),
                        jsonPath("$.locationId").value(1)
                ));
    }

    @Test
    @DisplayName("NotFoundException from Get one Ticket")
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
                        jsonPath("$.[0].eventId").value(1),
                        jsonPath("$.[0].locationId").value(1)
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
                        jsonPath("$", hasSize(1))
                ));
    }

    @Test
    @DisplayName("Save group Tickets")
    void saveGroupTickets() throws Exception {
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(TicketDto.builder()
                                .place(1)
                                .row(1)
                                .ticketStatus(TicketStatus.PURCHASED)
                                .eventId(1L)
                                .locationId(1L)
                                .userId(1L)
                                .build())
                                .collect(Collectors.toList()))))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("BadRequest from Save group Tickets")
    void saveGroupTicketsWithBadRequestException() throws Exception {
        mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(
                        Stream.of(new TicketDto())
                                .collect(Collectors.toList()))))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Update Ticket")
    void updateTicket() throws Exception {
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(TicketDto.builder()
                        .id(1L)
                        .place(1)
                        .row(1)
                        .locationId(1L)
                        .eventId(1L)
                        .userId(1L)
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build())
                ))
                .andExpect(status().isNoContent());
        mockMvc.perform(put("/ticket/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(TicketDto.builder()
                        .id(2L)
                        .place(1)
                        .row(1)
                        .locationId(1L)
                        .eventId(1L)
                        .userId(1L)
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build())
                ))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("NotFoundException from Update Ticket")
    void updateTicketWithNotFoundException() throws Exception {
        mockMvc.perform(put("/ticket/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(TicketDto.builder()
                        .id(2L)
                        .place(1)
                        .row(1)
                        .locationId(1L)
                        .eventId(1L)
                        .userId(1L)
                        .ticketStatus(TicketStatus.PURCHASED)
                        .build())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("BadRequest exception from Update Ticket")
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