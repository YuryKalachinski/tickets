package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/location")
public class LocationRestController {

    private static final Logger log = LoggerFactory.getLogger(LocationRestController.class);

    private final LocationService locationService;

    @Autowired
    public LocationRestController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getOneLocation(@PathVariable("id") Long id) {
        log.info("Get Location by id: {}", id);
        return new ResponseEntity<>(locationService.getLocationById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<Location>> getAllLocations() {
        log.info("Get all Locations");
        return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Location> saveLocation(@RequestBody Location location) {
        log.info("Save new Location: {}", location.toString());
        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(locationService.saveLocation(location).getId()).toUri();
//        URI locationUri = new URI(locationService.saveLocation(location).getId().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@RequestBody Location location, @PathVariable("id") Long id) {
        log.info("Update Location with id: {} with next body: {}", id, location.toString());
        locationService.updateLocation(location, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Long id) {
        log.info("Delete Location by id: {}", id);
        locationService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
