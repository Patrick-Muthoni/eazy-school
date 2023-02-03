package com.eazybytes.eazyschool.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@ToString
public class Contact {
    private String name;
    private String mobileNum;
    private String email;
    private String subject;
    private String message;

}
