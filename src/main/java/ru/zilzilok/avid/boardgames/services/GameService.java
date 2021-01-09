package ru.zilzilok.avid.boardgames.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.zilzilok.avid.boardgames.models.dto.BoardGameDto;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.boardgames.repositories.GameRepository;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.tools.OffsetBasedPageRequest;

import javax.transaction.Transactional;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepo;
    private final TeseraApiService teseraApiService;

    @Autowired
    public GameService(GameRepository gameRepository, TeseraApiService teseraApiService) {
        this.gameRepo = gameRepository;
        this.teseraApiService = teseraApiService;
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
    public BoardGame add(BoardGameDto boardGameDto) {
        BoardGame game = BoardGame.builder()
                .title(boardGameDto.getTitle())
                .description(boardGameDto.getDescription())
                .descriptionShort(boardGameDto.getDescriptionShort())
                .photoPath(boardGameDto.getPhotoUrl())
                .year(boardGameDto.getYear())
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
    public Iterable<BoardGame> addAllFromTesera() {
        addAll(teseraApiService.getAllGames());
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
