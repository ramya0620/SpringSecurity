package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.model.Loans;
import com.getvedbytes.ParivarBankApp.repository.LoanRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoansController {
    /*@GetMapping("/myLoans")
    public String getLoanDetails(){
        return "Here are the Loan details from DB";
    }*/
   private final LoanRepository loanRepository;
    @GetMapping("/myLoans")
    //@PostAuthorize("hasRole('ROOT')")
    @PostAuthorize("hasRole('USER')")
    public List<Loans> getLoanDetails(@RequestParam Long id){
        List<Loans> loans=loanRepository.findByCustomerIdOrderByStartDtDesc(id);
        if(loans!=null) {
            return loans;
        }else{
            return null;
        }
    }
}
