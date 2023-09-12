package com.gaurav.filesearchapiepassi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FileSearchApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileSearchApiApplication.class, args);
    }

}
