package com.getvedbytes.ParivarBankApp.config;


import com.getvedbytes.ParivarBankApp.exceptions.CustomAccessDeniedHandler;
import com.getvedbytes.ParivarBankApp.exceptions.CustomBasicAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest.permitAll()) -->permitting all requesting ,not for production*/
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest.denyAll()) -->denying all requesting, not for production*/
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
         /*disabling the formlogin
            http.formLogin(flc->flc.disable());*/
        http.formLogin(withDefaults());
        //http.httpBasic(withDefaults());
        http.httpBasic(hbc->hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));//for httpbasic(not global)
        http.exceptionHandling(hbc->hbc.accessDeniedHandler(new CustomAccessDeniedHandler()));//global level handling for denials
        return http.build();
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withUsername("user").password("{noop}12345").authorities("read").build();
        //UserDetails userDetails1=User.withUsername("admin").password("{noop}54321").authorities("admin").build();
        UserDetails userDetails1 = User.withUsername("admin").password("{bcrypt}$2a$12$0G60.9OXIddqXMO.1oBauOFSbOZUG/55VUyI50uq.T1m5II5P3Diu").authorities("admin").build();
        return new InMemoryUserDetailsManager(userDetails, userDetails1);
    }*/

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        /*UserDetails userDetails = User.withUsername("user").password("{noop}12345").authorities("read").build();
//        //UserDetails userDetails1=User.withUsername("admin").password("{noop}54321").authorities("admin").build();
//        UserDetails userDetails1 = User.withUsername("admin").password("{bcrypt}$2a$12$0G60.9OXIddqXMO.1oBauOFSbOZUG/55VUyI50uq.T1m5II5P3Diu").authorities("admin").build();*/
//        return new JdbcUserDetailsManager(dataSource);
//    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
        /*@Bean
        public CompromisedPasswordChecker compromisedPasswordChecker(){
            return new HaveIBeenPwnedRestApiPasswordChecker();
        }*/

}
