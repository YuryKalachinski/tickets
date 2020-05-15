package com.kalachinski.tickets.repositories;

import com.kalachinski.tickets.domains.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
