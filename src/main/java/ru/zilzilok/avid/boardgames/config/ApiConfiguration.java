package ru.zilzilok.avid.boardgames.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.zilzilok.avid.boardgames.services.api.ApiService;
import ru.zilzilok.avid.boardgames.services.api.TeseraApiService;

@Configuration
public class ApiConfiguration {

    private static final String TESERA_API_URL = "https://api.tesera.ru";

    @Bean
    public WebClient webClient() {
        return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build())
                .baseUrl(TESERA_API_URL)
                .build();
    }

    @Bean
    public ApiService apiService() {
        return new TeseraApiService(webClient(), gson());
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
