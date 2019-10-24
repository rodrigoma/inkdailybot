package com.rodrigoma.inkdailybot.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThisWillActuallyRun {

    @RequestMapping("/")
    public String home() {
        return "Hello World!";
    }
}