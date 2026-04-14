package com.poncebollmann.spider_tool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManualController {

    @GetMapping("/manual")
    public String manual() {
        return "manual";
    }
}