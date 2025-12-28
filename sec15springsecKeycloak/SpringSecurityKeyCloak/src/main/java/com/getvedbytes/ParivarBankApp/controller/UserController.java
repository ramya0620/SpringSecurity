package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.model.Customer;
import com.getvedbytes.ParivarBankApp.model.Customers;
import com.getvedbytes.ParivarBankApp.repository.CustomerRepository;
import com.getvedbytes.ParivarBankApp.repository.CustomersRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

   // CustomersRepository customersRepository;
   private final CustomerRepository customerRepository;

   @RequestMapping("/user")
    public Customer getUserDeatilsAfterLogin(Authentication authentication){
        Optional<Customer> optionalCustomer=customerRepository.findByEmail(authentication.getName());
        return optionalCustomer.orElse(null);
    }

}
