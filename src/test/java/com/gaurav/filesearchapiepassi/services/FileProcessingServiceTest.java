package com.gaurav.filesearchapiepassi.services;

import com.gaurav.filesearchapiepassi.model.WordFrequency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FileProcessingServiceTest {

    @Autowired
    private FileProcessingService fileProcessingService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        fileProcessingService = new FileProcessingService();
    }

    @Test
    public void testFindTopKFrequentWords() throws IOException {

        MultipartFile multipartFile = new MockMultipartFile("file",
                "test.txt", "text/plain", Files.readAllBytes(Paths.get("src/test/resources/test.txt")));

        // Define the expected WordFrequency objects
        List<WordFrequency> expectedWordFrequencies = Arrays.asList(
                new WordFrequency("Hello", 2),
                new WordFrequency("world", 1),
                new WordFrequency("file", 1),
                new WordFrequency("test", 1),
                new WordFrequency("this", 1)
        );


        // Call the method
        Mono<List<WordFrequency>> resultMono = fileProcessingService.findTopKFrequentWords(multipartFile, 5);

        // Create a StepVerifier to test the Mono
        StepVerifier.create(resultMono)
                .expectNextMatches(actualWordFrequencies -> {
                    assertEquals(expectedWordFrequencies.size(), actualWordFrequencies.size());
                    assertTrue(actualWordFrequencies.containsAll(expectedWordFrequencies));
                    return true;
                })
                .expectComplete()
                .verify();

    }
}
