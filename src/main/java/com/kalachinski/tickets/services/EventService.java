package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Optional<Event> getEventById(Long id);

    Event saveEvent(Event event);

    void deleteEvent(Long id);

    List<Event> getAllEvents();
}
