package com.vedget.funschool.config;

import com.vedget.funschool.handler.CustomAuthenticationDeniedHandler;
import com.vedget.funschool.handler.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
@RequiredArgsConstructor
public class ProjectSecurityConfig {

    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationDeniedHandler authenticationDeniedHandler;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/", "/home", "/holidays/**", "/contact", "/saveMsg",
                                "/courses", "/about", "/assets/**","/login/**").permitAll())
                //.formLogin(Customizer.withDefaults()) --> it should open the configured login page but by default opening spring security login page
                .formLogin(flc->flc.loginPage("/login").//1.this is our customized login page(login controller
                        usernameParameter("user"). //3.below username and pasword we have by default(UsernaePasswordAuthetication token check the variable)
                        passwordParameter("secretpwd"). //if needed we can customize,with username  and passwordparameter we have mentioned here,same needs to be update in login.html,otherwise no response/error
                        defaultSuccessUrl("/dashboard").// 2.if login is success then by default redirect to dashboard
                        failureUrl("/login?error=true").
                        successHandler(authenticationSuccessHandler).//handlers are samelike ddefault but we can control the code, more flexibility compare to the default urls
                        failureHandler(authenticationDeniedHandler)).
                logout(lfc->lfc.logoutSuccessUrl("/login?=logout").
                                invalidateHttpSession(true).
                                clearAuthentication(true).
                                deleteCookies("JSESSIONID"));
        http   .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}Funschool@123").authorities("read").build();
        UserDetails admin = User.withUsername("admin")
                .password("{bcrypt}$2a$12$lTYlTQiN6IrCONoRpqG1Iueh9FZB4rlRuYs0XXgOTZF/vPhVMxGF2")
                .authorities("admin").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * From Spring Security 6.3 version
     *
     * @return
     */
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }


}
