package com.gaurav.filesearchapiepassi.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WordFrequency {
    private String word;
    private int frequency;

    @JsonCreator
    public WordFrequency(@JsonProperty("word") String word, @JsonProperty("frequency") int frequency) {
        this.word = word;
        this.frequency = frequency;
    }
}
