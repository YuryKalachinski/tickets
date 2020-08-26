package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.dto.LocationDto;
import com.kalachinski.tickets.dto.ViewsDto;
import com.kalachinski.tickets.mappers.LocationMapper;
import com.kalachinski.tickets.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/location")
@Validated
public class LocationRestController {

    private static final Logger log = LoggerFactory.getLogger(LocationRestController.class);

    private final LocationService locationService;

    private final LocationMapper locationMapper;

    @Autowired
    public LocationRestController(LocationService locationService, LocationMapper locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getOneLocation(@PathVariable("id") Long id) {
        log.info("Get Location by id: {}", id);
        return new ResponseEntity<>(
                locationMapper.convertToDto(locationService.getLocationById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        log.info("Get all Locations");
        return new ResponseEntity<>(((List<Location>) locationService.getAllLocations()).stream()
                .map(locationMapper::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    @Validated(ViewsDto.New.class)
    public ResponseEntity<LocationDto> saveLocation(
            @RequestBody @Valid LocationDto locationDto) {
        log.info("Save new Location: {}", locationDto.toString());
        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(locationService.saveLocation(
                        locationMapper.convertToEntity(locationDto)).getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Validated(ViewsDto.Exist.class)
    public ResponseEntity<LocationDto> updateLocation(
            @RequestBody @Valid LocationDto locationDto,
            @PathVariable("id") Long id) {
        log.info("Update Location with id: {} with next body: {}", id, locationDto.toString());
        locationService.updateLocation(locationMapper.convertToEntity(locationDto), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LocationDto> deleteLocation(@PathVariable("id") Long id) {
        log.info("Delete Location by id: {}", id);
        locationService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}