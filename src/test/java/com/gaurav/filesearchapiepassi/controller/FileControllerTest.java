package com.gaurav.filesearchapiepassi.controller;

import com.gaurav.filesearchapiepassi.model.WordFrequency;
import com.gaurav.filesearchapiepassi.services.FileProcessingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebFluxTest(FileController.class)
@ActiveProfiles("test")
public class FileControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FileProcessingService fileProcessingService;

    @Test
    public void testFileUpload() {
        // Prepare a mock file for upload
        byte[] fileContent = "Hello, world!".getBytes(StandardCharsets.UTF_8);
        MultipartFile mockFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, fileContent);


//        MultipartBodyBuilder builder = new MultipartBodyBuilder();
//        builder.part("file", mockFile);
//        builder.part("k",5);
//        MultiValueMap<String, HttpEntity<?>> parts = builder.build();

        // Mock the fileProcessingService's response
        List<WordFrequency> expectedResponse = Arrays.asList(
                new WordFrequency("hello", 2),
                new WordFrequency("world", 1)
        );
        Mockito.when(fileProcessingService.findTopKFrequentWords(mockFile, 2)).thenReturn(Mono.just(expectedResponse));

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new ClassPathResource("test.txt"))
                .contentType(MediaType.MULTIPART_FORM_DATA);


        webTestClient.post()
                .uri("/api/fileupload")
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build())
                        .with("k","2"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(WordFrequency.class)
                .isEqualTo(expectedResponse);

//        webTestClient.post()
//                .uri("/upload")
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .bodyValue(parts -> {
//                    parts.with("file", filePart -> {
//                        filePart.headers(headers -> headers.setContentType(MediaType.MULTIPART_FORM_DATA));
//                        filePart.transferTo(mockFile.toPath());
//                    });
//                    parts.with("k", "2");
//                }) // You can set any value for k
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(WordFrequency.class)
//                .isEqualTo(expectedResponse);
    }

//    @Test
//    public void multipartFormDataWorks() {
//        ClassPathResource img = new ClassPathResource("test.txt");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        HttpEntity<ClassPathResource> entity = new HttpEntity<>(img, headers);
//        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
//        parts.add("file", entity);
//        Mono<Map> result = webTestClient.post()
//                .uri("/post")
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(parts))
//                .exchange()
//                .flatMap(response -> response.body(toMono(Map.class)));
//
//        StepVerifier.create(result)
//                .consumeNextWith(map -> {
//                    Map<String, Object> files = getMap(map, "files");
//                    assertThat(files).containsKey("imgpart");
//                    String file = (String) files.get("imgpart");
//                    assertThat(file).startsWith("data:").contains(";base64,");
//                })
//                .expectComplete()
//                .verify(DURATION);
//    }

}
