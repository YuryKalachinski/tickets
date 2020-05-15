package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.exceptions.BadRequestException;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Ticket getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(NotFoundException::new);
        log.info("Return Ticket by id: {}", id);
        return ticket;
    }

    @Override
    @Transactional
    public Iterable<Ticket> saveGroupTickets(List<Ticket> tickets) {
        List<Ticket> listTicket = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getTicketStatus() == null || ticket.getRow() == null ||
                    ticket.getPlace() == null || ticket.getLocation() == null || ticket.getEvent() == null) {
                throw new BadRequestException();
            }
            Ticket ticketFromDB = ticketRepository.save(ticket);
            listTicket.add(ticketFromDB);
            log.info("Ticket success saved with body: {}", ticketFromDB.toString());
        }
        if (listTicket.isEmpty()) {
            log.info("Transactional success complete with empty body");
        } else {
            log.info("Transactional success complete");
        }
        return listTicket;
    }

    @Override
    public void updateTicket(Ticket ticket, Long id) {
        ticketRepository.findById(id).orElseThrow(NotFoundException::new);
        if (ticket.getTicketStatus() == null || ticket.getRow() == null ||
                ticket.getPlace() == null || ticket.getLocation() == null || ticket.getEvent() == null) {
            throw new BadRequestException();
        }
        if (!id.equals(ticket.getId())) {
            ticket.setId(id);
        }
        Ticket ticketFromDB = ticketRepository.save(ticket);
        log.info("Ticket success updated with body: {}", ticketFromDB);
    }

    @Override
    public void deleteTicket(Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            log.info("Ticket success deleted by id: {}", id);
        } else {
            log.info("Ticket with id: {} is absent", id);
        }
    }

    @Override
    public Iterable<Ticket> getAllTickets() {
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
