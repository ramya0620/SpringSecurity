package com.parivarbank.springsecOAUTH2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity){
        httpSecurity.authorizeHttpRequests((request)->request.requestMatchers("/secure").authenticated()
                .anyRequest().permitAll());
        httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.oauth2Login(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository(){
        ClientRegistration github=gitHubClientRegistration();
        //ClientRegistration facebook=faceBookClientRegistration();
        return new InMemoryClientRegistrationRepository(github);
    }

    @Bean
    ClientRegistration gitHubClientRegistration(){
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
                                            .clientId("Ov23li4FyMOR9xEPrVog").
                                            clientSecret("79e6c482a2bbb5403cd7c0efdcfd48b92d01e5c3").build();
    }
    /*@Bean
    ClientRegistration faceBookClientRegistration(){
        return CommonOAuth2Provider.FACEBOOK.getBuilder("facebook")
                .clientId("").
                clientSecret("").build();
    }*/
}
