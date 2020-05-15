package com.kalachinski.tickets.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {

    Logger log = LoggerFactory.getLogger(MainController.class);

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal UserDetails user) {
        HashMap<Object, Object> data = new HashMap<>();
        if (user != null) {
            log.info("Authorization in application with login: {}", user.getUsername());
            data.put("profile", user);
        }
        model.addAttribute("frontEndData", data);
        return "main";
    }
}
