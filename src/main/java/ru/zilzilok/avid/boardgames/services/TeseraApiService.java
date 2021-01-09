package ru.zilzilok.avid.boardgames.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.dom4j.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import ru.zilzilok.avid.boardgames.models.dto.BoardGameDto;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * https://api.tesera.ru/help/index.html
 */

@Service
public class TeseraApiService {

    private static final String URL = "https://api.tesera.ru";
    private static final Type BOARD_GAME_DTO_TYPE = new TypeToken<ArrayList<BoardGameDto>>(){}.getType();
    private final WebClient webClient;
    private final Gson gson = new Gson();

    @Bean
    public WebClient webClient() {
        return WebClient.create(URL);
    }

    @Autowired
    public TeseraApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Transactional
    public Iterable<BoardGameDto> getAllGames() {
        List<BoardGameDto> games = new ArrayList<>();

        boolean dataExist = true;
        while (dataExist) {
            String response = webClient.get()
                    .uri(String.format("games?offset=%d&limit=100", 0))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (!ObjectUtils.isEmpty(response)) {
                games.addAll(Objects.requireNonNull(gson.fromJson(response, BOARD_GAME_DTO_TYPE)));
            } else {
                dataExist = false;
            }
        }

        return games;
    }
}
