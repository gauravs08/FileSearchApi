package com.gaurav.filesearchapiepassi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaurav.filesearchapiepassi.model.WordFrequency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {

    public static final String URL_FILE_UPLOAD = "/api/file/upload";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testApiFileUploadWithBasicAuthSuccess() throws Exception {
        ResultMatcher ok = status().isOk();

        String fileName = "test.txt";

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName,
                "text/plain", "Hello world hello world".getBytes());

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(URL_FILE_UPLOAD)
                        .file(mockMultipartFile)
                        .param("k", "10")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + "YWRtaW46cGFzc3dvcmQ=");


        // Define the expected WordFrequency objects
        List<WordFrequency> expectedWordFrequencies = Arrays.asList(
                new WordFrequency("world", 2),
                new WordFrequency("Hello", 1),
                new WordFrequency("hello", 1)
        );


        MvcResult result = this.mockMvc.perform(builder)
                .andExpect(ok)
                .andExpect(request().asyncStarted())
                .andReturn();

        MvcResult mvcResult = this.mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andReturn();

        // Extract the response content as a string
        String jsonResponse = mvcResult.getResponse().getContentAsString();

        // Create an ObjectMapper to deserialize the JSON response
        ObjectMapper objectMapper = new ObjectMapper();

        // Deserialize the JSON response into a list of WordFrequency objects
        List<WordFrequency> actualWordFrequencies = objectMapper.readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );

        // Verify that the actual and expected lists have the same elements
        assertEquals(expectedWordFrequencies.size(), actualWordFrequencies.size());
        assertTrue(actualWordFrequencies.containsAll(expectedWordFrequencies));
    }

    @Test
    public void testApiFileUploadAuthenticationDecline() throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(URL_FILE_UPLOAD)
                                .with(SecurityMockMvcRequestPostProcessors.httpBasic("hacker", "wrong_password"))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();
    }
}
