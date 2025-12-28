package com.getvedbytes.ParivarBankApp.config;


import com.getvedbytes.ParivarBankApp.exceptions.CustomAccessDeniedHandler;
import com.getvedbytes.ParivarBankApp.exceptions.CustomBasicAuthenticationEntryPoint;
import com.getvedbytes.ParivarBankApp.filters.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfig {


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler=new CsrfTokenRequestAttributeHandler();

        http.
                sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                cors(corsConfig->corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config=new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/contact","/register","/apiLogin")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class).
                addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class).
                addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class).
                 addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class).
                addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class).
                addFilterBefore(new JwtTokenValidationFilter (), BasicAuthenticationFilter.class).
                requiresChannel(rcc->rcc.anyRequest().requiresInsecure()).
                authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/myAccounts").hasAuthority("VIEWACCOUNT")
//                        .requestMatchers( "/myLoans").hasAuthority("VIEW LOANS")
//                        .requestMatchers( "/myBalance").hasAnyAuthority("VIEWBALANCE","VIEWACCOUNT")
//                        .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
//                        .requestMatchers("/user").authenticated().
                        .requestMatchers("/myAccount").hasRole("USER")
                        .requestMatchers( "/myBalance").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/myLoans").authenticated()
                        .requestMatchers("/myCards").hasRole("USER")
                        .requestMatchers("/user").authenticated().
                         requestMatchers("/contact", "/register","/invalidSession","/notices","/apiLogin").
                          permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc->hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));//for httpbasic(not global)
        http.exceptionHandling(hbc->hbc.accessDeniedHandler(new CustomAccessDeniedHandler()));//global level handling for denials
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                 PasswordEncoder passwordEncoder)
    {
        ParivarBankUsernamePwdAuthenticationProvider usernamePwdAuthenticationProvider=new ParivarBankUsernamePwdAuthenticationProvider(userDetailsService,passwordEncoder);
        ProviderManager providerManager=new ProviderManager(usernamePwdAuthenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;

    }

}
