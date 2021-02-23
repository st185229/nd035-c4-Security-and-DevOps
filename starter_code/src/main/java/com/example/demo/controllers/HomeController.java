package com.example.demo.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "Greetings from Ecommerce Udacity Project";
    }
}
