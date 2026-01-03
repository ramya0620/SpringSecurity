package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.Constants.ApplicationConstants;
import com.getvedbytes.ParivarBankApp.model.Customer;
import com.getvedbytes.ParivarBankApp.model.LoginRequestDTO;
import com.getvedbytes.ParivarBankApp.model.LoginResponseDTO;
import com.getvedbytes.ParivarBankApp.repository.CustomerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.boot.logging.log4j2.Log4J2LoggingSystem.getEnvironment;

@RestController
@RequiredArgsConstructor
public class UserController {

    // CustomersRepository customersRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final Environment env;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customers) {
        try {
            String hashpwd = passwordEncoder.encode(customers.getPwd());
            customers.setPwd(hashpwd);
            customers.setCreateDt(new Date(System.currentTimeMillis()));
            Customer savedCustomers = customerRepository.save(customers);

            if (savedCustomers.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Given User deatils are Successfully registered");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("User registration failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occoured " + ex.getMessage());
        }
    }

    @GetMapping("/user")
    public Customer getUserDeatilsAfterLogin(Authentication authentication) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(authentication.getName());
        return optionalCustomer.orElse(null);
    }

    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDTO> apiLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequestDTO.username(), loginRequestDTO.password());
        Authentication authentication1 = authenticationManager.authenticate(authentication);
        if (authentication1 != null || authentication1.isAuthenticated()) {

            if (env != null) {
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                String jwtToken = Jwts.builder().issuer("ParivarBank").
                        subject("JwtToken").
                        claim("username", authentication1.getName()).
                        claim("authorities", authentication1.getAuthorities().stream().
                                map(GrantedAuthority::getAuthority).collect(Collectors.joining(","))).
                        issuedAt(new java.util.Date()).
                        expiration(new java.util.Date(new java.util.Date().getTime() + 300000)).signWith(secretKey).compact();
            }

        }
        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_AUTH_HEADER, jwt).
                body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }

}

