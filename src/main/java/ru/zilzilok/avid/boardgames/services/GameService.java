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
import ru.zilzilok.avid.profiles.models.other.UserGame;
import ru.zilzilok.avid.tools.OffsetBasedPageRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        BoardGame boardGame = game.orElse(null);
        if(boardGame != null){
            return addAverageRating(boardGame);
        }
        return null;
    }

    @Transactional
    public BoardGame findByAlias(String alias) {
        BoardGame boardGame = gameRepo.findByAlias(alias);
        if(boardGame != null){
            return addAverageRating(boardGame);
        }
        return null;
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
                .playtimeMin(boardGameDto.getPlaytimeMin())
                .playtimeMax(boardGameDto.getPlaytimeMax())
                .playersAgeMin(boardGameDto.getPlayersAgeMin())
                .build();
        gameRepo.save(game);
    }

    @Transactional
    public void addAllGames(List<BoardGameDto> boardGameDtoList) {
        for (BoardGameDto game : boardGameDtoList) {
            addGame(game);
        }
    }

    @Transactional
    public Iterable<BoardGame> addAllFromApi() {
        addAllGames(apiService.getAllGames());
        log.info("All games have been added.");
        return addAverageRating(getAllGames(10, 0));
    }

    @Transactional
    public Iterable<BoardGame> addAllFromApi(boolean sortByBGGRate, int limit) {
        List<BoardGameDto> boardGameDtoList = apiService.getAllGames(sortByBGGRate, limit);
        addAllGames(boardGameDtoList);
        log.info(boardGameDtoList.size() + " games have been added.");
        return addAverageRating(getAllGames(10, 0));
    }

    @Transactional
    public Iterable<BoardGame> getAllGames(int limit, int offset, Sort sort, String title) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit, sort);
        return addAverageRating(gameRepo.findByTitlesContainingIgnoreCase(title, pageable));
    }

    @Transactional
    public Iterable<BoardGame> getAllGames(int limit, int offset, Sort sort) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit, sort);
        return addAverageRating(gameRepo.findAll(pageable).getContent());
    }

    @Transactional
    public Iterable<BoardGame> getAllGames(int limit, int offset) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit);
        return addAverageRating(gameRepo.findAll(pageable).getContent());
    }

    private Iterable<BoardGame> addAverageRating(Iterable<BoardGame> boardGames) {
        boardGames.forEach(boardGame -> {
            List<Double> ratings = boardGame.getOwners().stream().map(UserGame::getRating).collect(Collectors.toList());
            double averageRating = ratings.size() > 0 ? ratings.stream().reduce(0., Double::sum) / ratings.size() : 0;
            boardGame.setAverageRating(averageRating);
        });
        return boardGames;
    }

    private BoardGame addAverageRating(BoardGame boardGame) {
        List<Double> ratings = boardGame.getOwners().stream().map(UserGame::getRating).collect(Collectors.toList());
        double averageRating = ratings.size() > 0 ? ratings.stream().reduce(0., Double::sum) / ratings.size() : 0;
        boardGame.setAverageRating(averageRating);
        return boardGame;
    }
}
