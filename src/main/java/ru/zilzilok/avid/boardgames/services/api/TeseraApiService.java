package ru.zilzilok.avid.boardgames.services.api;

import com.google.gson.Gson;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.zilzilok.avid.boardgames.models.dto.BoardGameDto;
import ru.zilzilok.avid.boardgames.models.dto.TeseraBoardGameDto;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * https://api.tesera.ru/help/index.html
 */

@Service
@Log
public class TeseraApiService implements ApiService {

    private static final int LIMIT = 100;
    private final WebClient webClient;
    private final Gson gson;

    @Autowired
    public TeseraApiService(WebClient webClient, Gson gson) {
        this.webClient = webClient;
        this.gson = gson;
    }

    @Override
    @Transactional
    public List<BoardGameDto> getAllGames() {
        log.info("Start TeseraApi getAllGames...");
        ArrayList<BoardGameDto> games = new ArrayList<>();
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
            log.info(MessageFormat.format("{0} response: {1} entities were received.", i, newGames.length));
            if (newGames.length != 0) {
                for (BoardGameDto gameDto : newGames) {
                    if (StringUtils.isNotBlank(gameDto.getAlias())
                            && StringUtils.isNotBlank(gameDto.getDescription())
                            && !games.contains(gameDto)) {
                        games.add(gameDto);
                    }
                }
            } else {
                dataExist = false;
            }
        }
        log.info(MessageFormat.format("RESULT: {0} games were received.", games.size()));
        return games;
    }

    @Override
    public List<BoardGameDto> getAllGames(boolean sortByBGGRate, int limit) {
        log.info("Start TeseraApi getAllGames(boolean sortByBGGRate, int limit)...");
        log.info(sortByBGGRate ? "Reload by rating." : "Reload not by rating.");
        ArrayList<BoardGameDto> games = new ArrayList<>();
        boolean dataExist = true;
        int i = 0;
        String pattern = "games?offset=%d&limit=%d" + (sortByBGGRate ? "&sort=-ratingn10" : "");
        while (dataExist && games.size() < limit) {
            String response = webClient.get()
                    .uri(String.format(pattern, i++, LIMIT))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            BoardGameDto[] newGames = gson.fromJson(response, TeseraBoardGameDto[].class);
            log.info(MessageFormat.format("{0} response: {1} entities were received.", i, newGames.length));
            if (newGames.length != 0) {
                for (BoardGameDto gameDto : newGames) {
                    if (StringUtils.isNotBlank(gameDto.getAlias())
                            && StringUtils.isNotBlank(gameDto.getDescription())
                            && !games.contains(gameDto)) {
                        games.add(gameDto);
                        if(games.size() == limit) {
                            break;
                        }
                    }
                }
            } else {
                dataExist = false;
            }
        }
        log.info(MessageFormat.format("RESULT: {0} games were received.", games.size()));
        return games;
    }
}
