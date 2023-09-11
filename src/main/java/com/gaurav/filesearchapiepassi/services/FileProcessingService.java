package com.gaurav.filesearchapiepassi.services;

import com.gaurav.filesearchapiepassi.Exception.FileFormatException;
import com.gaurav.filesearchapiepassi.Exception.LargeFileProcessingException;
import com.gaurav.filesearchapiepassi.model.WordFrequency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        List<Map.Entry<String, Integer>> sortedList = wordFrequencyMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .toList();

        List<WordFrequency> topWords = sortedList.stream()
                .limit(k)
                .map(entry -> new WordFrequency(entry.getKey(), entry.getValue()))
                .toList();

        log.debug("File most frequent words:" + topWords);
        return Mono.just(topWords);
    }

    private static List<WordFrequency> getWordFrequenciesDescOrder(PriorityQueue<Map.Entry<String, Integer>> minHeap) {
        List<WordFrequency> topWords = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            Map.Entry<String, Integer> entry = minHeap.poll();
            topWords.add(new WordFrequency(entry.getKey(), entry.getValue()));
        }

        // Reverse the list to get the top K words in descending order of frequency
        Collections.reverse(topWords);
        return topWords;
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
        //while ((line = reader.readLine()) != null) {
//                List<String> words = new ArrayList<>();
//
//                // Use a regular expression to match words and exclude tabs and newlines
//                Pattern pattern = Pattern.compile("\\b\\w+\\b");
//                Matcher matcher = pattern.matcher(line);
//
//                while (matcher.find()) {
//                    String word = matcher.group();
//                    // Remove punctuation and convert to lowercase for accurate word counting
//                    words.add(word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim());
//                }
        log.info("WORDS IN LINE :"+ Arrays.stream(words).toList());
        for (String word : words) {
            //word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
        }
    }

}
