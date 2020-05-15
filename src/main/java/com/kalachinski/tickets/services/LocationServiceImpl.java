package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.exceptions.BadRequestException;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Location getLocationById(Long id) {
        Location location = locationRepository.findById(id).orElseThrow(NotFoundException::new);
        log.info("Return Location by id: {}", id);
        return location;
    }

    @Override
    public Iterable<Location> getAllLocations() {
        log.info("Return all Locations");
        return locationRepository.findAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Location saveLocation(Location location) {
        if (location.getName() == null || location.getNumberOfPlace() == null || location.getNumberOfRow() == null) {
            throw new BadRequestException();
        }
        Location locationFromDB = locationRepository.save(location);
        log.info("New Location success saved with body: {}", locationFromDB.toString());
        return locationFromDB;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateLocation(Location location, Long id) {
        locationRepository.findById(id).orElseThrow(NotFoundException::new);
        if (location.getName() == null || location.getNumberOfPlace() == null || location.getNumberOfRow() == null) {
            throw new BadRequestException();
        }
        if (!id.equals(location.getId())) {
            location.setId(id);
        }
        Location locationFromDB = locationRepository.save(location);
        log.info("Location success updated with body: {}", locationFromDB.toString());
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteLocation(Long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            log.info("Location success deleted by id: {}", id);
        } else {
            log.info("Location with id: {} is absent", id);
        }
    }
}
