package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Location;

public interface LocationService {
    Location getLocationById(Long id);

    Iterable<Location> getAllLocations();

    Location saveLocation(Location location);

    void updateLocation(Location location, Long id);

    void deleteLocation(Long id);
}
