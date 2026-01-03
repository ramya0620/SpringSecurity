package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.model.Contact;
import com.getvedbytes.ParivarBankApp.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactRepository contactRepository;
    @PostMapping("/contact")
    @PreFilter("filterObject.contactname!='test'")
    public Contact saveContactInquiryDetails(@RequestBody List<Contact> contacts){

        if(!contacts.isEmpty()){
            Contact contact=contacts.getFirst();
            contact.setContactId(getServiceReqNumber());
            contact.setCreateDt(new Date(System.currentTimeMillis()));
        return contactRepository.save(contact);
    }else{
            return null;
        }
    }
    public String getServiceReqNumber(){
        Random random =new Random();
        int ranNum= random.nextInt(999999999)+9999;
        return "Sr"+ranNum;
    }

}
