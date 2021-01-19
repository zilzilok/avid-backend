package ru.zilzilok.avid.boardgames.services;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
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
    public BoardGame add(BoardGameDto boardGameDto) {
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

        return gameRepo.save(game);
    }

    @Transactional
    public Iterable<BoardGame> addAll(Iterable<BoardGameDto> boardGameDtoList) {
        List<BoardGame> boardGameList = new ArrayList<>();
        for (BoardGameDto game : boardGameDtoList) {
            boardGameList.add(add(game));
        }
        return gameRepo.saveAll(boardGameList);
    }

    @Transactional
    public Iterable<BoardGame> addAllFromApi() {
        addAll(apiService.getAllGames());
        return getAll(10, 0);
    }

    @Transactional
    public Long count() {
        return gameRepo.count();
    }

    @Transactional
    public Iterable<BoardGame> getAll(int limit, int offset, Sort sort) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, sort);
        return gameRepo.findAll(pageable).getContent();
    }

    @Transactional
    public Iterable<BoardGame> getAll(int limit, int offset) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        return gameRepo.findAll(pageable).getContent();
    }
}
