package com.gaurav.filesearchapiepassi.services;

import com.gaurav.filesearchapiepassi.model.WordFrequency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
@Slf4j
public class TextProcessingService {


    public Mono<List<WordFrequency>> findTopKFrequentWords(MultipartFile file, int k) {
        // Create a map to store word frequencies
        Map<String, Integer> wordFrequencyMap = getWordFrequencyMap(file);

        // Sort the map by frequency in descending order
        List<Map.Entry<String, Integer>> sortedList = wordFrequencyMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .toList();

        // Extract the top K words
//        List<String> topWords = sortedList.stream()
//                .limit(k)
//                .map(Map.Entry::getKey)
//                .toList();

        List<WordFrequency> topWords = sortedList.stream()
                .limit(k)
                .map(entry -> new WordFrequency(entry.getKey(), entry.getValue()))
                .toList();

//        // Create a min-heap (PriorityQueue) to find the top K frequent words
//        PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(
//                Comparator.comparingInt(Map.Entry::getValue)
//        );
//
//        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
//            minHeap.offer(entry);
//            if (minHeap.size() > k) {
//                minHeap.poll(); // Remove the least frequent word if heap size exceeds K
//            }
//        }
//
//        // Create a list to store the top K words and their frequencies
//        List<WordFrequency> topWords = getWordFrequenciesDescOrder(minHeap);
//
//        // Save the top K words and their frequencies in the database
//        for (WordFrequency wordFrequency : topWords) {
//            wordFrequencyRepository.save(wordFrequency);
//        }
        log.debug("File most frequent words:"+ topWords);
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
                String[] words = line.trim().split("\\s+");
                for (String word : words) {
                    // Remove punctuation and convert to lowercase for accurate word counting
                    word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
                    wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (IOException e) {
            // Handle IO exception
            throw new RuntimeException("Error reading the file: " + e.getMessage());
        }
        return wordFrequencyMap;
    }


}
