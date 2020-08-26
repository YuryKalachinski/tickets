package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Ticket;

import java.util.List;

public interface TicketService {
    Ticket getTicketById(Long id);

    List<Ticket> saveGroupTickets(List<Ticket> tickets);

    void updateTicket(Ticket ticket, Long id);

    void deleteTicket(Long id);

    List<Ticket> getAllTickets();

    List<Ticket> getTicketsByEventId(Long eventId);

    List<Ticket> getTicketsByUserId(Long userId);
}
