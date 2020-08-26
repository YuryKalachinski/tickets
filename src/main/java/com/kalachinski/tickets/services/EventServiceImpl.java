package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(NotFoundException::new);
        log.info("Return Event by id: {}", id);
        return event;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Event saveEvent(Event event) {
        Event eventFromDB = eventRepository.save(event);
        log.info("New Event success saved with body: {}", eventFromDB.toString());
        return eventFromDB;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateEvent(Event event, Long id) {
        Event eventFromDB = eventRepository.findById(id)
                .map(ev -> {
                    ev.setName(event.getName());
                    ev.setDateTime(event.getDateTime());
                    ev.setLocation(event.getLocation());
                    return eventRepository.save(ev);
                }).orElseThrow(NotFoundException::new);
        log.info("Event success updated with body: {}", eventFromDB.toString());
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            log.info("Event success deleted by id: {}", id);
        } else {
            log.info("Event with id: {} is absent", id);
        }
    }

    @Override
    public List<Event> getAllEvents() {
        log.info("Return all Events");
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getEventsByLocationId(Long locationId) {
        log.info("Return all Events with Location id: {}", locationId);
        return eventRepository.findByLocation(Location.builder()
                .id(locationId)
                .build());
    }
}
