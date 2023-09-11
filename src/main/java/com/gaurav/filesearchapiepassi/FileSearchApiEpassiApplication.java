package com.gaurav.filesearchapiepassi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FileSearchApiEpassiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileSearchApiEpassiApplication.class, args);
    }

}
