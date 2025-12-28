package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.model.Customer;
import com.getvedbytes.ParivarBankApp.model.Customers;
import com.getvedbytes.ParivarBankApp.repository.CustomerRepository;
import com.getvedbytes.ParivarBankApp.repository.CustomersRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UserController {

   // CustomersRepository customersRepository;
    PasswordEncoder passwordEncoder;

   /* @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customers customers){
        try{
            String hashpwd=passwordEncoder.encode(customers.getPwd());
            customers.setPwd(hashpwd);
            Customers savedCustomers=customersRepository.save(customers);
            if(savedCustomers.getId()>0){
                return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
            }
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AN exception occoured"+ex.getMessage());
        }
    }*/
    private final CustomerRepository customerRepository;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customers){
        try{
            String hashpwd=passwordEncoder.encode(customers.getPwd());
            customers.setPwd(hashpwd);
            customers.setCreateDt(new Date(System.currentTimeMillis()));
            Customer savedCustomers=customerRepository.save(customers);

            if(savedCustomers.getId()>0){
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Given User deatils are Successfully registered");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("User registration failed");
            }
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occoured "+ex.getMessage());
        }
    }

    @RequestMapping("/user")
    public Customer getUserDeatilsAfterLogin(Authentication authentication){
        Optional<Customer> optionalCustomer=customerRepository.findByEmail(authentication.getName());
        return optionalCustomer.orElse(null);
    }

}
