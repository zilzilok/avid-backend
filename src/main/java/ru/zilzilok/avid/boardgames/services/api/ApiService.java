package ru.zilzilok.avid.boardgames.services.api;

import ru.zilzilok.avid.boardgames.models.dto.BoardGameDto;

public interface ApiService {
    Iterable<BoardGameDto> getAllGames();
}
