package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void saveContactDetails(Contact contact) {
        contact.setStatus(EazySchoolConstants.OPEN);
        contact.setCreatedAt(LocalDateTime.now());
        contact.setCreatedBy(EazySchoolConstants.ANONYMOUS);
        contactRepository.save(contact);
    }

    public List<Contact> getContactMessages(String status) {
        return contactRepository.findAllByStatus(status);
    }

    public Contact updateMsg(int contactId, String updatedBy) {
        Optional<Contact> actualContact = contactRepository.findById(contactId);
        if (actualContact.isPresent()) {
            Contact contact = actualContact.get();
            contact.setStatus(EazySchoolConstants.CLOSE);
            contact.setUpdatedBy(updatedBy);
            return contactRepository.save(contact);
        }
        return null;
    }
}
