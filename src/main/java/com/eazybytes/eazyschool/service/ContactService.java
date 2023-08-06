package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.config.EazySchoolProps;
import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContactService {

    private final ContactRepository contactRepository;
    private final EazySchoolProps eazySchoolProps;

    public ContactService(ContactRepository contactRepository, EazySchoolProps eazySchoolProps) {
        this.contactRepository = contactRepository;
        this.eazySchoolProps = eazySchoolProps;
    }

    public void saveContactDetails(Contact contact) {
        contact.setStatus(EazySchoolConstants.OPEN);
        contactRepository.save(contact);
    }

    public List<Contact> getContactMessages(String status) {
        return contactRepository.findAllByStatus(status);
    }

    public int updateMsg(int contactId) {


        return contactRepository.updateMsgStatus(EazySchoolConstants.CLOSE, contactId);
//        Optional<Contact> actualContact = contactRepository.findById(contactId);
//        if (actualContact.isPresent()) {
//            Contact contact = actualContact.get();
//            contact.setStatus(EazySchoolConstants.CLOSE);
//            return contactRepository.save(contact);
//        }
//        return null;
    }

    public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDir) {
        int pageSize = eazySchoolProps.getPageSize();

        if (eazySchoolProps.getContact() != null && eazySchoolProps.getContact().get("pageSize") != null) {
            pageSize = Integer.parseInt(eazySchoolProps.getContact().get("pageSize").trim());
        }

        Pageable pageable = PageRequest.of(pageNum-1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending(): Sort.by(sortField).descending());
        Page<Contact> msgPage = contactRepository.findOpenMsgs(EazySchoolConstants.OPEN, pageable);
        return msgPage;
    }
}
