package com.eazybytes.eazyschool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class DashboardController {

    @RequestMapping(value = "/dashboard")
    public String showDashboard(Authentication authentication, Model model) {

        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        throw new RuntimeException("We have a problem");

//        return "dashboard.html";
    }

}
