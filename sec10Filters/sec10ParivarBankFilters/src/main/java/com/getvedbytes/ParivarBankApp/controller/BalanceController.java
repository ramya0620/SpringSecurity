package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.model.AccountTransactions;
import com.getvedbytes.ParivarBankApp.repository.AccountTransactionsRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BalanceController {
    /*@GetMapping("/myBalance")
    public String getBalanceDetails(){
        return "Here are the Balance details from DB";
    }*/
     private final AccountTransactionsRepository accountTransactionsRepository;
    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetails(@RequestParam Long id){
        List<AccountTransactions> accountBalanceTransaction=accountTransactionsRepository.findByCustomerIdOrderByTransactionDtDesc(id);
        if(accountBalanceTransaction!=null){
            return accountBalanceTransaction;
        }else{
            return null;
        }
    }


}
