package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.dto.TicketDto;
import com.kalachinski.tickets.dto.ViewsDto;
import com.kalachinski.tickets.mappers.TicketMapper;
import com.kalachinski.tickets.services.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ticket")
@Validated
public class TicketRestController {

    private static final Logger log = LoggerFactory.getLogger(TicketRestController.class);

    private final TicketService ticketService;

    private final TicketMapper ticketMapper;

    @Autowired
    public TicketRestController(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        log.info("Get all Tickets");
        return new ResponseEntity<>(ticketService.getAllTickets().stream()
                .map(ticketMapper::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable("id") Long id) {
        log.info("Get Ticket by id: {}", id);
        return new ResponseEntity<>(ticketMapper.convertToDto(ticketService.getTicketById(id)), HttpStatus.OK);
    }

    @GetMapping(params = "eventId")
    public ResponseEntity<List<TicketDto>> getTicketsByEventId(@RequestParam Long eventId) {
        log.info("Get all Tickets with Event id: {}", eventId);
        return new ResponseEntity<>(ticketService.getTicketsByEventId(eventId).stream()
                .map(ticketMapper::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(params = "userId")
    public ResponseEntity<List<TicketDto>> getTicketsByUserId(@RequestParam Long userId) {
        log.info("Get all Tickets with User id: {}", userId);
        return new ResponseEntity<>(ticketService.getTicketsByUserId(userId).stream()
                .map(ticketMapper::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    @Validated(ViewsDto.New.class)
    public ResponseEntity<List<TicketDto>> saveGroupTickets(
            @RequestBody List<@Valid TicketDto> ticketsDto) {
        ticketsDto.forEach(ticketDto -> log.info("Save Ticket: {}", ticketDto.toString()));
        return new ResponseEntity<>(ticketService.saveGroupTickets(ticketsDto.stream()
                .map(ticketMapper::convertToEntity)
                .collect(Collectors.toList())).stream()
                .map(ticketMapper::convertToDto)
                .collect(Collectors.toList()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Validated(ViewsDto.Exist.class)
    public ResponseEntity<TicketDto> updateTicket(
            @RequestBody @Valid TicketDto ticketDto, @PathVariable("id") Long id) {
        log.info("Update Ticket with id: {} with next body: {}", id, ticketDto.toString());
        ticketService.updateTicket(ticketMapper.convertToEntity(ticketDto), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TicketDto> deleteTicket(@PathVariable("id") Long id) {
        log.info("Delete Ticket by id: {}", id);
        ticketService.deleteTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
