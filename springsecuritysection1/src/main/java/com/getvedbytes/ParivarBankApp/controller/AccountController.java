package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.model.Accounts;
import com.getvedbytes.ParivarBankApp.repository.AccountsRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountController {
   /* @GetMapping("/myAccounts")
    public String getAccountDetails(){
        return "Here are the Account details from DB";
    }*/
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
