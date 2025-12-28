package com.getvedbytes.ParivarBankApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Welcome {
    @GetMapping("/welcome")
    public String sayWelcome(){
        return "welcome to spring application without security";
    }
}
