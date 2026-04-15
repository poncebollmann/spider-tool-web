package com.poncebollmann.spider_tool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/revision")
    public String dashboard() {
        return "dashboard";
    }
}