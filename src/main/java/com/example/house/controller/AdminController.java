package com.example.house.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin/login";
    }
}
