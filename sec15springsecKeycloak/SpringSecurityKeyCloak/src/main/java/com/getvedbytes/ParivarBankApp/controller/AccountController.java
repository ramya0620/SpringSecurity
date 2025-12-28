package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.model.Accounts;
import com.getvedbytes.ParivarBankApp.repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountsRepository accountsRepository;
    @GetMapping("/myAccounts")
    public Accounts getAccountDetails(@RequestParam long id){
        Accounts accounts= accountsRepository.findByCustomerId(id);
        if(accounts!=null){
            return accounts;
        }else{
            return null;
        }
    }
}
