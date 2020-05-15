package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.services.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketRestController {

    private static final Logger log = LoggerFactory.getLogger(TicketRestController.class);

    private final TicketService ticketService;

    @Autowired
    public TicketRestController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Ticket>> getAllTickets() {
        log.info("Get all Tickets");
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable("id") Long id) {
        log.info("Get Ticket by id: {}", id);
        return new ResponseEntity<>(ticketService.getTicketById(id), HttpStatus.OK);
    }

    @GetMapping(params = "eventId")
    public ResponseEntity<List<Ticket>> getTicketsByEventId(@RequestParam Long eventId) {
        log.info("Get all Tickets with Event id: {}", eventId);
        return new ResponseEntity<>(ticketService.getTicketsByEventId(eventId), HttpStatus.OK);
    }

    @GetMapping(params = "userId")
    public ResponseEntity<List<Ticket>> getTicketsByUserId(@RequestParam Long userId) {
        log.info("Get all Tickets with User id: {}", userId);
        return new ResponseEntity<>(ticketService.getTicketsByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Iterable<Ticket>> saveGroupTickets(@RequestBody List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            log.info("Save Ticket: {}", ticket.toString());
        }
        return new ResponseEntity<>(ticketService.saveGroupTickets(tickets), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@RequestBody Ticket ticket, @PathVariable("id") Long id) {
        log.info("Update Ticket with id: {} with next body: {}", id, ticket.toString());
        ticketService.updateTicket(ticket, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ticket> deleteTicket(@PathVariable("id") Long id) {
        log.info("Delete Ticket by id: {}", id);
        ticketService.deleteTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
