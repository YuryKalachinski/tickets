package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.domains.User;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "/application_test.properties")
@Sql(value = {"/truncate_ticket.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/truncate_ticket.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TicketRepositoryTest {

    private final TestEntityManager entityManager;
    private final TicketRepository ticketRepository;

    @Autowired
    TicketRepositoryTest(TestEntityManager entityManager, TicketRepository ticketRepository) {
        this.entityManager = entityManager;
        this.ticketRepository = ticketRepository;
    }

    @Test
    @DisplayName("Find Ticket by Id")
    void findTicketById() {
        Ticket ticket = Ticket.builder().build();
        entityManager.persistAndFlush(ticket);
        Assertions.assertEquals(ticketRepository.findById(ticket.getId())
                .orElseThrow(NotFoundException::new), ticket);
    }

    @Test
    @DisplayName("Save group Tickets")
    void saveGroupTickets() {
        List<Ticket> ticketList = Stream.of(new Ticket()).collect(Collectors.toList());
        Assertions.assertEquals(ticketRepository.saveAll(ticketList), ticketList);
    }

    @Test
    @DisplayName("Update exist Ticket")
    void updateTicket() {
        Ticket ticket = Ticket.builder().build();
        entityManager.persistAndFlush(ticket);
        Ticket ticketFromDB = ticketRepository.findById(ticket.getId())
                .orElseThrow(NotFoundException::new);
        Assertions.assertEquals(ticketRepository.save(ticketFromDB), ticket);
    }

    @Test
    @DisplayName("Delete Ticket by Id")
    void deleteTicketById() {
        Ticket ticket = Ticket.builder().build();
        entityManager.persistAndFlush(ticket);
        ticketRepository.deleteById(ticket.getId());
        Assertions.assertTrue(ticketRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Exist Ticket by Id")
    void existTicketById() {
        Ticket ticket = Ticket.builder().build();
        entityManager.persistAndFlush(ticket);
        Assertions.assertTrue(ticketRepository.existsById(ticket.getId()));
    }

    @Test
    @DisplayName("Find All Tickets in empty repositories")
    void findAllTicketsInEmptyRepositories() {
        Assertions.assertTrue(ticketRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Find All Tickets")
    void findAllTickets() {
        Ticket ticket = Ticket.builder().build();
        entityManager.persistAndFlush(ticket);
        Assertions.assertEquals(ticketRepository.findAll(), Stream.of(ticket).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Find Tickets by Event")
    @Sql(value = {"/truncate_ticket.sql",
            "/truncate_event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/truncate_ticket.sql",
            "/truncate_event.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findTicketsByEvent() {
        Event event = Event.builder().build();
        entityManager.persistAndFlush(event);
        Ticket ticket  = Ticket.builder()
                .event(event)
                .build();
        entityManager.persistAndFlush(ticket);
        Assertions.assertEquals(ticketRepository.findByEvent(event), Stream.of(ticket).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Find Tickets by User")
    @Sql(value = {"/truncate_ticket.sql",
            "/truncate_user.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/truncate_ticket.sql",
            "/truncate_user.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findTicketsByUser() {
        User user = User.builder().build();
        entityManager.persistAndFlush(user);
        Ticket ticket  = Ticket.builder()
                .user(user)
                .build();
        entityManager.persistAndFlush(ticket);
        Assertions.assertEquals(ticketRepository.findByUser(user), Stream.of(ticket).collect(Collectors.toList()));
    }
}