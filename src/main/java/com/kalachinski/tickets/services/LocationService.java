package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    Optional<Location> getLocationById(Long id);

    Location saveLocation(Location location);

    void deleteLocation(Long id);

    List<Location> getAllLocations();
}
