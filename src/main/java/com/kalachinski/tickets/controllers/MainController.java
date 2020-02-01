package com.kalachinski.tickets.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = "/hello")
    public String hello() {
        return "helloPage";
    }
}
