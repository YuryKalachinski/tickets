package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Location;

import java.util.List;

public interface LocationService {
    Location getLocationById(Long id);

    List<Location> getAllLocations();

    Location saveLocation(Location location);

    void updateLocation(Location location, Long id);

    void deleteLocation(Long id);
}
