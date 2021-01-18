package ru.zilzilok.avid.boardgames.services.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.zilzilok.avid.boardgames.models.dto.BoardGameDto;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * https://api.tesera.ru/help/index.html
 */

@Service
public class TeseraApiService implements ApiService {

    private static final Type BOARD_GAME_DTO_TYPE = new TypeToken<ArrayList<BoardGameDto>>() {
    }.getType();
    private static final Gson gson = new Gson();
    private final WebClient webClient;

    @Autowired
    public TeseraApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    @Transactional
    public Iterable<BoardGameDto> getAllGames() {
        List<BoardGameDto> games = new ArrayList<>();

        boolean dataExist = true;
        int i = 0;
        while (dataExist) {
            String response = webClient.get()
                    .uri(String.format("games?offset=%d&limit=100", i++))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (StringUtils.isNotEmpty(response)) {
                games.addAll(Objects.requireNonNull(gson.fromJson(response, BOARD_GAME_DTO_TYPE)));
            } else {
                dataExist = false;
            }
        }

        return games;
    }
}
