package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/location")
public class LocationRestController {
    private LocationService locationService;

    @Autowired
    public LocationRestController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Location> getOneLocation(@PathVariable("id") Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Location> saveLocation(@RequestBody Location location) {
        if (!Optional.ofNullable(location).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(locationService.saveLocation(location), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Location> updateLocation(@RequestBody Location location, @PathVariable("id") Long id) {
        if (locationService.getLocationById(id).isPresent()
                && Optional.ofNullable(location).isPresent()
                && id.equals(location.getId())) {
            return new ResponseEntity<>(locationService.saveLocation(location), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Long id) {
        if (locationService.getLocationById(id).isPresent()) {
            locationService.deleteLocation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
