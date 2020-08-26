package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(NotFoundException::new);
        log.info("Return Ticket by id: {}", id);
        return ticket;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public List<Ticket> saveGroupTickets(List<Ticket> tickets) {
        List<Ticket> listTicket = (List<Ticket>)ticketRepository.saveAll(tickets);
        log.info("Transactional success complete");
        listTicket.forEach(ticket -> log.info("Ticket success saved with body: {}", ticket.toString()));
        return listTicket;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateTicket(Ticket ticket, Long id) {
        Ticket ticketFromDB = ticketRepository.findById(id)
                .map(ev -> {
                    ev.setPlace(ticket.getPlace());
                    ev.setRow(ticket.getRow());
                    ev.setEvent(ticket.getEvent());
                    ev.setLocation(ticket.getLocation());
                    ev.setTicketStatus(ticket.getTicketStatus());
                    ev.setUser(ticket.getUser());
                    return ticketRepository.save(ev);
                }).orElseThrow(NotFoundException::new);
        log.info("Ticket success updated with body: {}", ticketFromDB);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteTicket(Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            log.info("Ticket success deleted by id: {}", id);
        } else {
            log.info("Ticket with id: {} is absent", id);
        }
    }

    @Override
    public List<Ticket> getAllTickets() {
        log.info("Return all Tickets");
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> getTicketsByEventId(Long eventId) {
        log.info("Return all Tickets with Event id: {}", eventId);
        return ticketRepository.findByEvent(Event.builder()
                .id(eventId)
                .build());
    }

    @Override
    public List<Ticket> getTicketsByUserId(Long userId) {
        log.info("Return all Tickets with User id: {}", userId);
        return ticketRepository.findByUser(User.builder()
                .id(userId)
                .build());
    }
}
