package com.getvedbytes.ParivarBankApp.repository;

import com.getvedbytes.ParivarBankApp.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
	
	
}