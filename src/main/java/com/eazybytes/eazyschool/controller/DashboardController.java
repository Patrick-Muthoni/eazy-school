package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class DashboardController {

    @Value("${eazyschool.pageSize}")
    private int defaultPageSize;

    @Value("${eazyschool.contact.successMsg}")
    private String message;

    private final PersonRepository personRepository;

    public DashboardController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @RequestMapping(value = "/dashboard")
    public String showDashboard(Authentication authentication, Model model, HttpSession httpSession) {
        Person person = personRepository.readByEmail(authentication.getName());
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        if (person.getEazyClass() != null && person.getEazyClass().getName() != null) {
            model.addAttribute("enrolledClass", person.getEazyClass().getName());
        }
        httpSession.setAttribute("loggedInPerson", person);
        log.error("defaultPageSize " + defaultPageSize);
        log.error("Successmessage " + message);
        return "dashboard.html";
    }

}
