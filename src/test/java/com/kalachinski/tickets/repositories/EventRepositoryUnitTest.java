package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "/application_test.properties")
@Sql(value = {"/truncate_event.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/truncate_event.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class EventRepositoryUnitTest {

    private final TestEntityManager entityManager;
    private final EventRepository eventRepository;

    @Autowired
    EventRepositoryUnitTest(TestEntityManager entityManager, EventRepository eventRepository) {
        this.entityManager = entityManager;
        this.eventRepository = eventRepository;
    }

    @Test
    @DisplayName("Find Event by Id")
    void findEventById() {
        Event event = Event.builder().build();
        entityManager.persistAndFlush(event);
        Assertions.assertEquals(eventRepository.findById(event.getId())
                .orElseThrow(NotFoundException::new), event);
    }

    @Test
    @DisplayName("Save new Event")
    void saveEvent() {
        Event event = Event.builder()
                .name("Event")
                .build();
        Assertions.assertEquals(eventRepository.save(event).getName(), event.getName());
    }

    @Test
    @DisplayName("Update exist Event")
    void updateEvent() {
        Event event = Event.builder().build();
        entityManager.persistAndFlush(event);
        Event eventFromDB = eventRepository.findById(event.getId())
                .orElseThrow(NotFoundException::new);
        Assertions.assertEquals(eventRepository.save(eventFromDB), event);
    }

    @Test
    @DisplayName("Delete Event by Id")
    void deleteEventById() {
        Event event = Event.builder().build();
        entityManager.persistAndFlush(event);
        eventRepository.deleteById(event.getId());
        Assertions.assertTrue(eventRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Exist Event by Id")
    void existEventById() {
        Event event = Event.builder().build();
        entityManager.persistAndFlush(event);
        Assertions.assertTrue(eventRepository.existsById(event.getId()));
    }

    @Test
    @DisplayName("Find All Events in empty repositories")
    void findAllEventsInEmptyRepositories() {
        Assertions.assertTrue(eventRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Find All Events")
    void findAllEvents() {
        Event event = Event.builder().build();
        entityManager.persistAndFlush(event);
        Assertions.assertEquals(eventRepository.findAll(), Stream.of(event).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Find Events by Location")
    @Sql(value = {"/truncate_event.sql",
            "/truncate_location.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/truncate_event.sql",
            "/truncate_location.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findEventsByLocation() {
        Location location = Location.builder().build();
        entityManager.persistAndFlush(location);
        Event event = Event.builder()
                .location(location)
                .build();
        entityManager.persistAndFlush(event);
        Assertions.assertEquals(eventRepository.findByLocation(location), Stream.of(event).collect(Collectors.toList()));
    }
}