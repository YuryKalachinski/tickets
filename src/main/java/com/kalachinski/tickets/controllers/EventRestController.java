package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.exceptions.BadRequestException;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/event")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @GetMapping("{id}")
    public ResponseEntity<Event> getOneEvent(@PathVariable("id") Long id) {
        return new ResponseEntity<>(eventService.getEventById(id).orElseThrow(NotFoundException::new), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Event> saveEvent(@RequestBody Event event) {
        Optional.ofNullable(event).orElseThrow(BadRequestException::new);
        if (eventService.getEventById(event.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(eventService.saveEvent(event), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event, @PathVariable("id") Long id) {
        eventService.getEventById(id).orElseThrow(NotFoundException::new);
        Optional.ofNullable(event).orElseThrow(BadRequestException::new);
        if (!id.equals(event.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(eventService.saveEvent(event), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable("id") Long id) {
        eventService.getEventById(id).orElseThrow(NotFoundException::new);
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
