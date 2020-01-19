package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> getTicketById(Long id);

    Ticket saveTicket(Ticket ticket);

    void deleteTicket(Long id);

    List<Ticket> getAllTickets();
}
