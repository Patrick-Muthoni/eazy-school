package com.eazybytes.eazyschool.rest.controller;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.model.Response;
import com.eazybytes.eazyschool.repository.ContactRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/contact",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@CrossOrigin(origins = "*")
public class ContactRestController {
    private final ContactRepository contactRepository;

    public ContactRestController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @GetMapping("/getMessagesByStatus")
    public List<Contact> getMessagesByStatus(@RequestParam(name = "status") String status) {
        return contactRepository.findAllByStatus(status);
    }

    @GetMapping("/getAllMessagesByStatus")
    public List<Contact> getAllMessagesByStatus(@RequestBody Contact contact) {

        if (contact != null || !contact.getStatus().equals("")) {
            return contactRepository.findAllByStatus(contact.getStatus());

        } else {
           return List.of();
        }
    }

    @PostMapping("/saveMsg")
    public ResponseEntity<Response> saveContact(@RequestHeader("invocationFrom") String invocationFrom,
                                                @Valid @RequestBody Contact contact) {
        log.info(String.format("Received header %s", invocationFrom));
        contactRepository.save(contact);
        Response response = new Response(200, "Saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isCreated", "true")
                .body(response);

    }

    @DeleteMapping("/deleteMsg")
    public ResponseEntity<Response> deleteMsg(RequestEntity<Contact> request) {
        HttpHeaders headers = request.getHeaders();
        headers.forEach((key, value) -> {
            log.info(String.format("header %s, value %s", key, String.join("|", value)));
        });

        Contact contact = request.getBody();
        contactRepository.deleteById(contact.getContactId());
        Response response = new Response(200, "Deleted Successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/closeMsg")
    public ResponseEntity<Response> updateMsgStatus(@RequestBody Contact contact) {

        Response response = new Response();

        Optional<Contact> contact1 = contactRepository.findById(contact.getContactId());
        if (contact1.isPresent()) {
            contact1.get().setStatus(EazySchoolConstants.CLOSE);
            contactRepository.save(contact1.get());
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatusMsg("Updated successfully");
        } else {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setStatusMsg("Contact Id provided does not exist");
        }

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }
}
