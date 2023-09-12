package com.gaurav.filesearchapiepassi.controller;

import com.gaurav.filesearchapiepassi.Exception.FileNotFoundException;
import com.gaurav.filesearchapiepassi.model.ApiResponse;
import com.gaurav.filesearchapiepassi.model.WordFrequency;
import com.gaurav.filesearchapiepassi.services.FileProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/")
@Slf4j
public class FileController {
    private final FileProcessingService fileProcessingService;

    public FileController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @GetMapping("/users/welcome")
    public ResponseEntity<ApiResponse> handleFileUpload() {
        return new ResponseEntity<>(new ApiResponse(200, "Hello world!!!"), HttpStatus.OK);

    }

    @PostMapping(value = "/file/upload")
    public Mono<List<WordFrequency>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam int k) {
        // Validate the file and perform the upload
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a file to upload.");
        }
        return fileProcessingService.findTopKFrequentWords(file, k);
    }

    @GetMapping("/up")
    public ResponseEntity<ApiResponse> uploadFile() throws IOException {
        return new ResponseEntity<>(new ApiResponse(200, "UP"), HttpStatus.OK);
    }
}
