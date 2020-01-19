package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    List<Location> findAll();

    Optional<Location> findById(Long id);
}
