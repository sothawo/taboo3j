package com.sothawo.taboo3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
public class Taboo3jApplication {
    public static void main(String[] args) {
        SpringApplication.run(Taboo3jApplication.class, args);
    }
}

