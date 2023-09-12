package com.gaurav.filesearchapiepassi.services;

import com.gaurav.filesearchapiepassi.exception.FileFormatException;
import com.gaurav.filesearchapiepassi.model.WordFrequency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
@Slf4j
@CacheConfig(cacheNames = "wordFrequencyCache")
public class FileProcessingService {


    @Cacheable(key = "{#file.originalFilename, #k}")
    public Mono<List<WordFrequency>> findTopKFrequentWords(MultipartFile file, int k) {


        // Check if the uploaded file is a text file based on content type or file extension
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        if (contentType == null || !contentType.startsWith("text/") || !Objects.requireNonNull(fileName).toLowerCase().endsWith(".txt")) {
            throw new FileFormatException("Uploaded file is not a valid text file.");
        }

        // Create a map to store word frequencies
        Map<String, Integer> wordFrequencyMap = getWordFrequencyMap(file);

        // Sort the map by frequency in descending order
        List<WordFrequency> topWords = limitSortWordFrequencyMap(k, wordFrequencyMap);

        log.debug("File most frequent words:" + topWords);
        return Mono.just(topWords);
    }

    private static Map<String, Integer> getWordFrequencyMap(MultipartFile file) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Tokenize the line into words (split by whitespace)
                processLine(line, wordFrequencyMap);
            }
        } catch (IOException e) {
            // Handle IO exception
            throw new RuntimeException("Error reading the file: " + e.getMessage());
        }
        return wordFrequencyMap;
    }

    private static void processLine(String line, Map<String, Integer> wordFrequencyMap) {
        String[] words = line.trim().split("\\s+");
        log.info("WORDS IN LINE :" + Arrays.stream(words).toList());
        for (String word : words) {
            //word = word.replaceAll("[^a-zA-Z]", "").toLowerCase(); // This can be used for ignoring case
            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
        }
    }

    private static List<WordFrequency> limitSortWordFrequencyMap(int k, Map<String, Integer> wordFrequencyMap) {
        List<Map.Entry<String, Integer>> sortedList = wordFrequencyMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .toList();

        List<WordFrequency> topWords = sortedList.stream()
                .limit(k)
                .map(entry -> new WordFrequency(entry.getKey(), entry.getValue()))
                .toList();
        return topWords;
    }

}
