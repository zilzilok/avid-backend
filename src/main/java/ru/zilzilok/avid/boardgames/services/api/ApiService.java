package ru.zilzilok.avid.boardgames.services.api;

import ru.zilzilok.avid.boardgames.models.dto.BoardGameDto;

import java.util.List;

public interface ApiService {
    List<BoardGameDto> getAllGames();

    List<BoardGameDto> getAllGames(boolean sortByBGGRate, int limit);
}
