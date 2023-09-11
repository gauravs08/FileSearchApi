package com.gaurav.filesearchapiepassi.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WordFrequency {
    private String word;
    private int frequency;

    // Getters and setters
}
