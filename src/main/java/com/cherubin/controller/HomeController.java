package com.cherubin.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @GetMapping
    public String homeController() {
        return "Hello world";
    }

    //@PostMapping
    //@PutMapping
    //@DeleteMapping

}
