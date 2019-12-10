package com.kalachinski.tickets;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingRestController {

    @GetMapping("/hello")
    public String greeting() {
        return "Hello, user";
    }
}
