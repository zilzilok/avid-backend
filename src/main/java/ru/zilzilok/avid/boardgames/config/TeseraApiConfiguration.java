package ru.zilzilok.avid.boardgames.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TeseraApiConfiguration {
    private static final String URL = "https://api.tesera.ru";
    @Bean
    public WebClient webClient() {
        return WebClient.create(URL);
    }
}
