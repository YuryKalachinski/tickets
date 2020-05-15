package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

}
