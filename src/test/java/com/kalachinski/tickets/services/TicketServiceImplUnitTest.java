package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.TicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class TicketServiceImplUnitTest {

    private final TicketService ticketService;

    @Autowired
    TicketServiceImplUnitTest(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @MockBean
    private TicketRepository ticketRepository;

    @Test
    @DisplayName("Get Tickets by Id")
    void getTicketById() {
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        Assertions.assertEquals(ticketService.getTicketById(1L), ticket);
        verify(ticketRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("NotFoundException from Get Tickets by Id")
    void getTicketByIdWithNotFoundException() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> ticketService.getTicketById(1L));
        verify(ticketRepository, times(1)).findById(anyLong());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Save group Tickets")
    void saveGroupTickets() {
        List<Ticket> ticketList = new ArrayList<>();
        when(ticketRepository.saveAll(anyIterable())).thenReturn(ticketList);
        Assertions.assertEquals(ticketService.saveGroupTickets(ticketList), ticketList);
        verify(ticketRepository, times(1)).saveAll(anyIterable());
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Save group Tickets")
    void saveGroupTicketsWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> ticketService.saveGroupTickets(Stream.of(new Ticket()).collect(Collectors.toList())));
        verify(ticketRepository, never()).saveAll(anyIterable());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Update Ticket")
    void updateTicket() {
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        ticketService.updateTicket(ticket, 1L);
        verify(ticketRepository, times(1)).findById(anyLong());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("NotFoundException from Update Ticket")
    void updateTicketWithNotFoundException() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> ticketService.updateTicket(new Ticket(), 1L));
        verify(ticketRepository, times(1)).findById(anyLong());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Update Ticket")
    void updateTicketWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> ticketService.updateTicket(new Ticket(), 1L));
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Delete Exist Ticket")
    void deleteExistTicket() {
        when(ticketRepository.existsById(anyLong())).thenReturn(true);
        ticketService.deleteTicket(1L);
        verify(ticketRepository, times(1)).existsById(anyLong());
        verify(ticketRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Delete NotExist Ticket")
    void deleteNotExistTicket() {
        when(ticketRepository.existsById(anyLong())).thenReturn(false);
        ticketService.deleteTicket(1L);
        verify(ticketRepository, times(1)).existsById(anyLong());
        verify(ticketRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Delete Ticket")
    void deleteTicketWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> ticketService.deleteTicket(1L));
        verify(ticketRepository, never()).existsById(anyLong());
        verify(ticketRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Get All Tickets")
    void getAllTickets() {
        List<Ticket> ticketList = new ArrayList<>();
        when(ticketRepository.findAll()).thenReturn(ticketList);
        Assertions.assertEquals(ticketService.getAllTickets(), ticketList);
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get All Tickets by Event Id")
    void getTicketsByEventId() {
        List<Ticket> ticketList = new ArrayList<>();
        when(ticketRepository.findByEvent(any(Event.class))).thenReturn(ticketList);
        Assertions.assertEquals(ticketService.getTicketsByEventId(1L), ticketList);
        verify(ticketRepository, times(1)).findByEvent(any(Event.class));
    }

    @Test
    void getTicketsByUserId() {
        List<Ticket> ticketList = new ArrayList<>();
        when(ticketRepository.findByUser(any(User.class))).thenReturn(ticketList);
        Assertions.assertEquals(ticketService.getTicketsByUserId(1L), ticketList);
        verify(ticketRepository, times(1)).findByUser(any(User.class));
    }
}