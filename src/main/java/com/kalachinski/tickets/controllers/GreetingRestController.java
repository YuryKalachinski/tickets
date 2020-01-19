package com.kalachinski.tickets.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingRestController {

    @GetMapping("/")
    public String greeting() {
        return "Hello, guest";
    }
}
