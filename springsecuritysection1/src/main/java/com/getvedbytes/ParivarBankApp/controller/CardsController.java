package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.model.Cards;
import com.getvedbytes.ParivarBankApp.repository.CardsRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CardsController {

    /*@GetMapping("/myCards")
    public String getCardDetails(){
        return "Here are the Card details from DB";
    }*/
    private final CardsRepository cardsRepository;
    @GetMapping("/myCards")
    public List<Cards> getCardDetails(@RequestParam Long id){
        List<Cards> cards=cardsRepository.findByCustomerId(id);
        if(cards!=null){
            return cards;
        }
        else{
            return null;
        }
    }
}
