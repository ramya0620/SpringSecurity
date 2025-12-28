package com.getvedbytes.ParivarBankApp.config;

import com.getvedbytes.ParivarBankApp.exceptions.CustomAccessDeniedHandler;
import com.getvedbytes.ParivarBankApp.exceptions.CustomBasicAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.cors(corsConfig->corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config=new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setMaxAge(3600L);
                        return config;
                    }
                })).
                sessionManagement(smc->smc.invalidSessionUrl("/invalidSession").maximumSessions(3).maxSessionsPreventsLogin(true)).
                requiresChannel(rcc->rcc.anyRequest().requiresInsecure()).
                csrf((csrfConfigurer)->csrfConfigurer.disable()).
                authorizeHttpRequests((requests) -> requests.
                requestMatchers("/myAccounts", "/myLoans", "/myBalance", "/myCards","/user").
                authenticated().
                requestMatchers("/contacts", "/register","/invalidSession","/notices").
                permitAll());
        http.formLogin(withDefaults());

        http.httpBasic(hbc->hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));//for httpbasic(not global)
        http.exceptionHandling(hbc->hbc.accessDeniedHandler(new CustomAccessDeniedHandler()));//global level handling for denials
        return http.build();
    }




}
