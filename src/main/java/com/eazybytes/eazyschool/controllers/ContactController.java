package com.eazybytes.eazyschool.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ContactController {

    private static Logger logger = LoggerFactory.getLogger(ContactController.class);

    @RequestMapping({"/contact"})
    public String displayContactPage() {
        return "contact.html";
    }

    @RequestMapping(value = "/saveMsg", method = POST)
    public ModelAndView saveMessage (@RequestParam String name, @RequestParam String mobileNum,
                                     @RequestParam String email,@RequestParam String subject,
                                     @RequestParam String message) {


        logger.info("name is " + name);

        return new ModelAndView("redirect:/contact");

    }
}
