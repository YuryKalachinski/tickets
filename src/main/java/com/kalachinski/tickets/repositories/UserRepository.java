package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    Optional<User> findByLogin(String login);
}
