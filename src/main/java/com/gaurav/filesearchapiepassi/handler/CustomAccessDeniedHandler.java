package com.gaurav.filesearchapiepassi.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaurav.filesearchapiepassi.model.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ApiResponse apiResponse = new ApiResponse(403, "Authorization Failure, Please check your credentials");
        String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
