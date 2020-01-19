package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findAll();

    Optional<Event> findById(Long id);
}
