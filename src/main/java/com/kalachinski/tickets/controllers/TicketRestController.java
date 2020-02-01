package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.exceptions.BadRequestException;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ticket")
public class TicketRestController {
    private final TicketService ticketService;

    @Autowired
    public TicketRestController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Ticket> getOneTicket(@PathVariable("id") Long id) {
        return new ResponseEntity<>(ticketService.getTicketById(id).orElseThrow(NotFoundException::new), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ticket> saveTicket(@RequestBody Ticket ticket) {
        Optional.ofNullable(ticket).orElseThrow(BadRequestException::new);
        if (ticketService.getTicketById(ticket.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ticketService.saveTicket(ticket), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Ticket> updateTicket(@RequestBody Ticket ticket, @PathVariable("id") Long id) {
        ticketService.getTicketById(id).orElseThrow(NotFoundException::new);
        Optional.ofNullable(ticket).orElseThrow(BadRequestException::new);
        if (!id.equals(ticket.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ticketService.saveTicket(ticket), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Ticket> deleteTicket(@PathVariable("id") Long id) {
        ticketService.getTicketById(id).orElseThrow(NotFoundException::new);
        ticketService.deleteTicket(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
