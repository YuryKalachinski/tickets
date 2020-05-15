package com.kalachinski.tickets.services;

import com.kalachinski.tickets.config.WebSecurityConfig;
import com.kalachinski.tickets.domains.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.info("Try authorization  User by login: {}", login);
        return new UserDetailsImpl(userService.getUserByLogin(login).orElseThrow(() -> {
                    log.info("Not found User with login: " + login);
                    return new UsernameNotFoundException("User not found.");
                }
        ));
    }
}
