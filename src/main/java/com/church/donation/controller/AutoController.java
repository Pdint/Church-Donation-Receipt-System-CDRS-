package com.church.donation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AutoController {

    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }
}
