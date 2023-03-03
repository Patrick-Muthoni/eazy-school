package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("public")
public class PublicController {

    @GetMapping("/register")
    public String displayRegisterPage(Model model) {
        model.addAttribute("person", new Person() );
        return "register.html";
    }

    @PostMapping("/createUser")
    public String createPerson(@Valid @ModelAttribute("person") Person person,  Errors errors) {
        if(errors.hasErrors()){
            return "register.html";
        }

        return "redirect:/login?register=true";
    }
}
