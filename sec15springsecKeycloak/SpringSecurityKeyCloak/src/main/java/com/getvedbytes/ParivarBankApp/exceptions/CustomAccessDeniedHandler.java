package com.getvedbytes.ParivarBankApp.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LocalDateTime dateTime=LocalDateTime.now();
        String path=request.getRequestURI();
        String msg= (accessDeniedException!=null&&accessDeniedException.getMessage()!=null)?accessDeniedException.getMessage():"Unauthrorized error";
        response.setHeader("Eazybank-denied-reason", "authorization error");
        response.setContentType("application/json,charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        String jsonResponse=String.format("{\"timestamp\":\"%s\",\"status\":\"%d\",\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                dateTime,HttpStatus.FORBIDDEN.value(),HttpStatus.FORBIDDEN.getReasonPhrase(),msg,path);

        response.getWriter().write(jsonResponse);
    }
}
