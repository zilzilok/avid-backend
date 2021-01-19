package ru.zilzilok.avid.boardgames.services.api;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.JSONUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.zilzilok.avid.boardgames.models.dto.BoardGameDto;
import ru.zilzilok.avid.boardgames.models.dto.TeseraBoardGameDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * https://api.tesera.ru/help/index.html
 */

@Service
public class TeseraApiService implements ApiService {

    private static final Gson gson = new Gson();
    private static final int LIMIT = 100;
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
                    .uri(String.format("games?offset=%d&limit=%d", i++, LIMIT))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            BoardGameDto[] newGames = gson.fromJson(response, TeseraBoardGameDto[].class);
            if (newGames.length != 0) {
                for (BoardGameDto gameDto : newGames) {
                    if (StringUtils.isNotBlank(gameDto.getAlias())) {
                        games.add(gameDto);
                    }
                }
            } else {
                dataExist = false;
            }
        }

        return games;
    }
}
