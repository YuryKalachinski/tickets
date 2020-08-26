package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.dto.EventDto;
import com.kalachinski.tickets.dto.ViewsDto;
import com.kalachinski.tickets.mappers.EventMapper;
import com.kalachinski.tickets.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/event")
@Validated
public class EventRestController {

    private static final Logger log = LoggerFactory.getLogger(EventRestController.class);

    private final EventService eventService;

    private final EventMapper eventMapper;

    @Autowired
    EventRestController(EventService eventService, EventMapper eventMapper) {
        this.eventMapper = eventMapper;
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        log.info("Get all Events");
        return new ResponseEntity<>(eventService.getAllEvents().stream()
                .map(eventMapper::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(params = "locationId")
    public ResponseEntity<List<EventDto>> getEventsByLocationId(@RequestParam Long locationId) {
        log.info("Get all Events with Location id: {}", locationId);
        return new ResponseEntity<>(eventService.getEventsByLocationId(locationId).stream()
                .map(eventMapper::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getOneEvent(@PathVariable("id") Long id) {
        log.info("Get Event by id: {}", id);
        return new ResponseEntity<>(eventMapper.convertToDto(eventService.getEventById(id)), HttpStatus.OK);
    }

    @PostMapping
    @Validated(ViewsDto.New.class)
    public ResponseEntity<EventDto> saveEvent(
            @RequestBody @Valid EventDto eventDto) {
        log.info("Save new Event: {}", eventDto.toString());
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(eventService.saveEvent(
                        eventMapper.convertToEntity(eventDto)).getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Validated(ViewsDto.Exist.class)
    public ResponseEntity<EventDto> updateEvent(
            @RequestBody @Valid EventDto eventDto,
            @PathVariable("id") Long id) {
        log.info("Update Event with id: {} with next body: {}", id, eventDto.toString());
        eventService.updateEvent(eventMapper.convertToEntity(eventDto), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventDto> deleteEvent(@PathVariable("id") Long id) {
        log.info("Delete Event by id: {}", id);
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}