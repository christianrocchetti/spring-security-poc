package com.example.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unprotected")
public class UnprotectedController {

    @GetMapping
    public String unprotectedEndpointExample() {
        return "I'm unprotected";
    }
}
