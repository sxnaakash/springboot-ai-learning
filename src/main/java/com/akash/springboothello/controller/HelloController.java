package com.akash.springboothello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Akash!";
    }

    @GetMapping("/status")
    public String status() {
        return "Application Running";
    }
}