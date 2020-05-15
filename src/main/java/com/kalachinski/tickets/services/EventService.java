package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Event;

import java.util.List;

public interface EventService {
    Event getEventById(Long id);

    Event saveEvent(Event event);

    void updateEvent(Event event, Long id);

    void deleteEvent(Long id);

    Iterable<Event> getAllEvents();

    List<Event> getEventsByLocationId(Long locationId);
}
