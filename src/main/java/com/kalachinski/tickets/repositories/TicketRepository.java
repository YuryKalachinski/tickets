package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Ticket;
import com.kalachinski.tickets.domains.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {

    List<Ticket> findByEvent(Event event);

    List<Ticket> findByUser(User user);

    List<Ticket> findAll();
}
