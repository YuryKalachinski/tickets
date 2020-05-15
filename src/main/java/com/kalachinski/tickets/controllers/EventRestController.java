package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/event")
public class EventRestController {

    private static final Logger log = LoggerFactory.getLogger(EventRestController.class);

    private final EventService eventService;

    @Autowired
    EventRestController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Event>> getAllEvents() {
        log.info("Get all Events");
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping(params = "locationId")
    public ResponseEntity<List<Event>> getEventsByLocationId(@RequestParam Long locationId) {
        log.info("Get all Events with Location id: {}", locationId);
        return new ResponseEntity<>(eventService.getEventsByLocationId(locationId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getOneEvent(@PathVariable("id") Long id) {
        log.info("Get Event by id: {}", id);
        return new ResponseEntity<>(eventService.getEventById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Event> saveEvent(@RequestBody Event event) {
        log.info("Save new Event: {}", event.toString());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(eventService.saveEvent(event).getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event, @PathVariable("id") Long id) {
        log.info("Update Event with id: {} with next body: {}", id, event.toString());
        eventService.updateEvent(event, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable("id") Long id) {
        log.info("Delete Event by id: {}", id);
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
