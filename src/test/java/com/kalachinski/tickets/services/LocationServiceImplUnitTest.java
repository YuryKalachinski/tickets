package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class LocationServiceImplUnitTest {

    private final LocationService locationService;

    @Autowired
    LocationServiceImplUnitTest(LocationService locationService) {
        this.locationService = locationService;
    }

    @MockBean
    private LocationRepository locationRepository;

    @Test
    @DisplayName("Get Location by Id")
    void getLocationById() {
        Location location = new Location();
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        Assertions.assertEquals(locationService.getLocationById(1L), location);
        verify(locationRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("NotFoundException from Get Location by Id")
    void getLocationByIdWithNotFoundException() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> locationService.getLocationById(1L));
        verify(locationRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Get All Locations")
    void getAllLocations() {
        List<Location> locationList = Stream.of(new Location()).collect(Collectors.toList());
        when(locationRepository.findAll()).thenReturn(locationList);
        Assertions.assertEquals(locationService.getAllLocations(), locationList);
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Save Location")
    @WithMockUser(authorities = "ADMIN")
    void saveLocation() {
        Location location = new Location();
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        Assertions.assertEquals(locationService.saveLocation(location), location);
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Save Location")
    void saveLocationWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> locationService.saveLocation(new Location()));
        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    @DisplayName("Update Location")
    @WithMockUser(authorities = "ADMIN")
    void updateLocation() {
        Location location = new Location();
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        locationService.updateLocation(location, 1L);
        verify(locationRepository, times(1)).findById(anyLong());
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    @DisplayName("NotFoundException from Update Location")
    @WithMockUser(authorities = "ADMIN")
    void updateLocationWithNotFoundException() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> locationService.updateLocation(new Location(),1L));
        verify(locationRepository, times(1)).findById(anyLong());
        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Update Location")
    void updateLocationWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> locationService.updateLocation(new Location(), 1L));
        verify(locationRepository, never()).findById(anyLong());
        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    @DisplayName("Delete Exist Location")
    @WithMockUser(authorities = "ADMIN")
    void deleteExistLocation() {
        when(locationRepository.existsById(anyLong())).thenReturn(true);
        locationService.deleteLocation(1L);
        verify(locationRepository, times(1)).existsById(anyLong());
        verify(locationRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete NotExist Location")
    @WithMockUser(authorities = "ADMIN")
    void deleteNotExistLocation() {
        when(locationRepository.existsById(anyLong())).thenReturn(false);
        locationService.deleteLocation(1L);
        verify(locationRepository, times(1)).existsById(anyLong());
        verify(locationRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Delete Location")
    void deleteLocationWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> locationService.deleteLocation(1L));
        verify(locationRepository, never()).existsById(anyLong());
        verify(locationRepository, never()).deleteById(anyLong());
    }
}