package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.Location;
import com.kalachinski.tickets.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "/application_test.properties")
@Sql(value = {"/truncate_location.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/truncate_location.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LocationRepositoryTest {

    private final TestEntityManager entityManager;

    private final LocationRepository locationRepository;

    @Autowired
    LocationRepositoryTest(TestEntityManager testEntityManager, LocationRepository locationRepository) {
        this.entityManager = testEntityManager;
        this.locationRepository = locationRepository;
    }

    @Test
    @DisplayName("Find Location by Id")
    void findLocationById() {
        Location location = Location.builder().build();
        entityManager.persistAndFlush(location);
        Assertions.assertEquals(locationRepository.findById(location.getId())
                .orElseThrow(NotFoundException::new), location);
    }

    @Test
    @DisplayName("Find All Locations in empty repositories")
    void findAllLocationsInEmptyRepositories() {
        Assertions.assertTrue(locationRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Find All Locations")
    void findAllLocations() {
        Location location = Location.builder().build();
        entityManager.persistAndFlush(location);
        Assertions.assertEquals(locationRepository.findAll(), Stream.of(location).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Save new Location")
    void saveLocation() {
        Location location = Location.builder()
                .name("Location")
                .build();
        Assertions.assertEquals(locationRepository.save(location).getName(), location.getName());
    }

    @Test
    @DisplayName("Update exist Location")
    void updateLocation() {
        Location location = Location.builder().build();
        entityManager.persistAndFlush(location);
        Location locationFromDB = locationRepository.findById(location.getId())
                .orElseThrow(NotFoundException::new);
        Assertions.assertEquals(locationRepository.save(locationFromDB), location);
    }

    @Test
    @DisplayName("Delete Location by Id")
    void deleteLocationById() {
        Location location = Location.builder().build();
        entityManager.persistAndFlush(location);
        locationRepository.deleteById(location.getId());
        Assertions.assertTrue(locationRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Exist Location by Id")
    void existLocationById() {
        Location location = Location.builder().build();
        entityManager.persistAndFlush(location);
        Assertions.assertTrue(locationRepository.existsById(location.getId()));
    }
}