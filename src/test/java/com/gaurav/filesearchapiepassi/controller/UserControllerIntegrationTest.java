package com.gaurav.filesearchapiepassi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    public static final String URL_USER_WELCOME = "/api/users/welcome";
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testWelcomeEndpointWithBasicAuthSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL_USER_WELCOME)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + "dXNlcjpwYXNzd29yZA=="))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello world!!!"));
    }

    @Test
    public void testAuthenticationDecline() throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(URL_USER_WELCOME)
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("hacker", "wrong_password"))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();
    }
}
