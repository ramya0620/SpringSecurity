package com.getvedbytes.ParivarBankApp.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEvents {
    @EventListener
    public void onSuccessEvent(AuthenticationSuccessEvent successEvent){
      log.info("Login  success for the user:{}",successEvent.getAuthentication().getName());
    }
    @EventListener
    public void onFailureEvent(AbstractAuthenticationFailureEvent failureEvent){
        log.error("Login  failed for the user:{} due to:{}",failureEvent.getAuthentication().getName(),
                failureEvent.getException().getMessage());
    }
}
