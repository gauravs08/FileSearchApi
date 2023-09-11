package com.gaurav.filesearchapiepassi.services;
import com.gaurav.filesearchapiepassi.model.WordFrequency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;




public class TextProcessingServiceTest {

    @Autowired
    private TextProcessingService textProcessingService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        textProcessingService = new TextProcessingService();
    }

    @Test
    public void testFindTopKFrequentWords() throws IOException {
        // Mock the MultipartFile
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("Hello world, hello world!\n".getBytes()));

        // Call the method
        Mono<List<WordFrequency>> resultMono = textProcessingService.findTopKFrequentWords(mockFile, 5);

        // Create a StepVerifier to test the Mono
        StepVerifier.create(resultMono)
                .expectNextMatches(wordFrequencies -> {
                    // Verify that the expected words and frequencies are in the result
                    assertEquals(2, wordFrequencies.size());
                    assertEquals("world", wordFrequencies.get(0).getWord());
                    assertEquals(2, wordFrequencies.get(0).getFrequency());
                    assertEquals("hello", wordFrequencies.get(1).getWord());
                    assertEquals(2, wordFrequencies.get(1).getFrequency());
                    return true;
                })
                .expectComplete()
                .verify();

    }
}
