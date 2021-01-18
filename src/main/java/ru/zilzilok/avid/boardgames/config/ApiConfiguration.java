package ru.zilzilok.avid.boardgames.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.zilzilok.avid.boardgames.services.api.ApiService;
import ru.zilzilok.avid.boardgames.services.api.TeseraApiService;

@Configuration
public class ApiConfiguration {

    private static final String TESERA_API_URL = "https://api.tesera.ru";

    @Bean
    public WebClient webClient() {
        return WebClient.create(TESERA_API_URL);
    }

    @Bean
    public ApiService apiService() {
        return new TeseraApiService(webClient());
    }
}
