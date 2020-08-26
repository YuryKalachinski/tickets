package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class EventServiceImplUnitTest {

    private final EventService eventService;

    @Autowired
    EventServiceImplUnitTest(EventService eventService) {
        this.eventService = eventService;
    }

    @MockBean
    private EventRepository eventRepository;

    @Test
    @DisplayName("Get Event by Id")
    void getEventById() {
        Event event = new Event();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        Assertions.assertEquals(eventService.getEventById(1L), event);
        verify(eventRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("NotFoundException from Get Event by Id")
    void getLocationByIdWithNotFoundException() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> eventService.getEventById(1L));
        verify(eventRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Save Event")
    @WithMockUser(authorities = "ADMIN")
    void saveEvent() {
        Event event = new Event();
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        Assertions.assertEquals(eventService.saveEvent(event), event);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Save Event")
    void saveEventWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> eventService.saveEvent(new Event()));
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    @DisplayName("Update Event")
    @WithMockUser(authorities = "ADMIN")
    void updateEvent() {
        Event event = new Event();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);
        eventService.updateEvent(event, 1L);
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("NotFoundException from Update Event")
    @WithMockUser(authorities = "ADMIN")
    void updateEventWithNotFoundException() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> eventService.updateEvent(new Event(),1L));
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Update Event")
    void updateEventWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> eventService.updateEvent(new Event(), 1L));
        verify(eventRepository, never()).findById(anyLong());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    @DisplayName("Delete Exist Event")
    @WithMockUser(authorities = "ADMIN")
    void deleteExistEvent() {
        when(eventRepository.existsById(anyLong())).thenReturn(true);
        eventService.deleteEvent(1L);
        verify(eventRepository, times(1)).existsById(anyLong());
        verify(eventRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete NotExist Event")
    @WithMockUser(authorities = "ADMIN")
    void deleteNotExistLocation() {
        when(eventRepository.existsById(anyLong())).thenReturn(false);
        eventService.deleteEvent(1L);
        verify(eventRepository, times(1)).existsById(anyLong());
        verify(eventRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Delete Event")
    void deleteEventWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> eventService.deleteEvent(1L));
        verify(eventRepository, never()).existsById(anyLong());
        verify(eventRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Get All Events")
    void getAllEvents() {
        List<Event> eventList = Stream.of(new Event()).collect(Collectors.toList());
        when(eventRepository.findAll()).thenReturn(eventList);
        Assertions.assertEquals(eventService.getAllEvents(), eventList);
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get All Events by Location ID")
    void getEventsByLocationId() {
        List<Event> eventList = Stream.of(new Event()).collect(Collectors.toList());
        when(eventRepository.findByLocation(any(Location.class))).thenReturn(eventList);
        Assertions.assertEquals(eventService.getEventsByLocationId(1L), eventList);
        verify(eventRepository, times(1)).findByLocation(any(Location.class));
    }
}