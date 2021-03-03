package ru.zilzilok.avid.boardgames.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.zilzilok.avid.boardgames.models.dto.BoardGameDto;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.boardgames.repositories.GameRepository;
import ru.zilzilok.avid.boardgames.services.api.ApiService;
import ru.zilzilok.avid.tools.OffsetBasedPageRequest;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Log
public class GameService {
    private final GameRepository gameRepo;
    private final ApiService apiService;

    @Autowired
    public GameService(GameRepository gameRepository, ApiService apiService) {
        this.gameRepo = gameRepository;
        this.apiService = apiService;
    }

    @Transactional
    public void deleteAll() {
        gameRepo.deleteAll();
    }

    @Transactional
    public BoardGame findById(Long id) {
        Optional<BoardGame> game = gameRepo.findById(id);
        return game.orElse(null);
    }

    @Transactional
    public BoardGame findByAlias(String alias) {
        return gameRepo.findByAlias(alias);
    }

    @Transactional
    public void addGame(BoardGameDto boardGameDto) {
        BoardGame game = BoardGame.builder()
                .titles(boardGameDto.getTitles())
                .description(boardGameDto.getDescription())
                .descriptionShort(boardGameDto.getDescriptionShort())
                .photoUrl(boardGameDto.getPhotoUrl())
                .year(boardGameDto.getYear())
                .alias(boardGameDto.getAlias())
                .playersMin(boardGameDto.getPlayersMin())
                .playersMax(boardGameDto.getPlayersMax())
                .playersMinRecommend(boardGameDto.getPlayersMinRecommend())
                .playersMaxRecommend(boardGameDto.getPlayersMaxRecommend())
                .build();
        gameRepo.save(game);
    }

    @Transactional
    public void addAllGames(Iterable<BoardGameDto> boardGameDtoList) {
        for (BoardGameDto game : boardGameDtoList) {
            addGame(game);
        }
    }

    @Transactional
    public Iterable<BoardGame> addAllFromApi() {
        addAllGames(apiService.getAllGames());
        log.info("All games have been added.");
        return getAllGames(10, 0);
    }

    @Transactional
    public Long count() {
        return gameRepo.count();
    }

    @Transactional
    public Iterable<BoardGame> getAllGames(int limit, int offset, Sort sort) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit, sort);
        return gameRepo.findAll(pageable).getContent();
    }

    @Transactional
    public Iterable<BoardGame> getAllGames(int limit, int offset) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit);
        return gameRepo.findAll(pageable).getContent();
    }
}
