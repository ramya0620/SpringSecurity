package com.getvedbytes.ParivarBankApp.config;


import com.getvedbytes.ParivarBankApp.exceptions.CustomAccessDeniedHandler;
import com.getvedbytes.ParivarBankApp.exceptions.CustomBasicAuthenticationEntryPoint;
import com.getvedbytes.ParivarBankApp.filters.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class ProjectSecurityProdConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler=new CsrfTokenRequestAttributeHandler();

        http.securityContext(contextConfig->contextConfig.requireExplicitSave(false)).
                sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)).
                cors(corsConfig->corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config=new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                 .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                 .ignoringRequestMatchers("/contact","/register")
                 .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                 .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class).
                  requiresChannel(rcc -> rcc.anyRequest().requiresSecure()).//accepts only HTTPS
                  authorizeHttpRequests((requests) -> requests
//                .requestMatchers("/myAccounts").hasAuthority("VIEWACCOUNT")
//                .requestMatchers( "/myLoans").hasAuthority("VIEW LOANS")
//                .requestMatchers( "/myBalance").hasAnyAuthority("VIEWBALANCE","VIEWACCOUNT")
//                .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                .requestMatchers("/myAccounts").hasRole("USER")
                .requestMatchers( "/myLoans").hasRole("USER")
                .requestMatchers( "/myBalance").hasAnyRole("USER","ADMIN")
                .requestMatchers("/myCards").hasRole("USER")
                .requestMatchers("/user").authenticated().
                  requestMatchers("/contacts", "/notices","/register").
                  permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc->hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(hbc->hbc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
        @Bean
        public CompromisedPasswordChecker compromisedPasswordChecker(){
            return new HaveIBeenPwnedRestApiPasswordChecker();
        }

}
