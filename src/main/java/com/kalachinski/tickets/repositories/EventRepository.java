package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.Event;
import com.kalachinski.tickets.domains.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findByLocation(Location location);
}
