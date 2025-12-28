package com.getvedbytes.ParivarBankApp.service;

import com.getvedbytes.ParivarBankApp.model.Customers;
import com.getvedbytes.ParivarBankApp.repository.CustomersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class ParivarbankService implements UserDetailsService {

    CustomersRepository customersRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Customers customers= customersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(" User not found in db" + username));
        List<GrantedAuthority> authorities=List.of(new SimpleGrantedAuthority(customers.getRole()));
        return new User(customers.getEmail(),customers.getPwd(),authorities) ;
    }
}
