package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.User;
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
@Sql(value = {"/truncate_user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/truncate_user.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {

    private final TestEntityManager entityManager;
    private final UserRepository userRepository;

    @Autowired
    UserRepositoryTest(TestEntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("Find User by Id")
    void findUserById() {
        User user = User.builder().build();
        entityManager.persistAndFlush(user);
        Assertions.assertEquals(userRepository.findById(user.getId())
                .orElseThrow(NotFoundException::new), user);
    }

    @Test
    @DisplayName("Save new User")
    void saveUser() {
        User user = User.builder()
                .lastName("LastName")
                .build();
        Assertions.assertEquals(userRepository.save(user).getLastName(), user.getLastName());
    }

    @Test
    @DisplayName("Update exist User")
    void updateUser() {
        User user = User.builder().build();
        entityManager.persistAndFlush(user);
        User userFromDB = userRepository.findById(user.getId())
                .orElseThrow(NotFoundException::new);
        Assertions.assertEquals(userRepository.save(userFromDB), user);
    }

    @Test
    @DisplayName("Delete User by Id")
    void deleteUserById() {
        User user = User.builder().build();
        entityManager.persistAndFlush(user);
        userRepository.deleteById(user.getId());
        Assertions.assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Exist User by Id")
    void existUserById() {
        User user = User.builder().build();
        entityManager.persistAndFlush(user);
        Assertions.assertTrue(userRepository.existsById(user.getId()));
    }

    @Test
    @DisplayName("Find User by Login")
    void findUserByLogin() {
        User user = User.builder()
                .login("Login")
                .build();
        entityManager.persistAndFlush(user);
        Assertions.assertEquals(userRepository.findByLogin("Login")
                .orElseThrow(NotFoundException::new), user);
    }

    @Test
    @DisplayName("Find All Users in empty repositories")
    void findAllUsersInEmptyRepositories() {
        Assertions.assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Find All Users")
    void findAllUsers() {
        User user = User.builder().build();
        entityManager.persistAndFlush(user);
        Assertions.assertEquals(userRepository.findAll(), Stream.of(user).collect(Collectors.toList()));
    }
}