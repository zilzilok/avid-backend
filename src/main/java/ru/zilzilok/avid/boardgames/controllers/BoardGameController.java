package ru.zilzilok.avid.boardgames.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.boardgames.models.dto.UserBoardGameDto;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.boardgames.services.GameService;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserGameService;
import ru.zilzilok.avid.profiles.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/games")
public class BoardGameController {

    private final GameService gameService;
    private final UserService userService;
    private final UserGameService userGameService;

    @Autowired
    public BoardGameController(GameService gameService, UserService userService, UserGameService userGameService) {
        this.gameService = gameService;
        this.userService = userService;
        this.userGameService = userGameService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardGame> getGame(@PathVariable("id") Long id) {
        return ResponseEntity.ok(gameService.findById(id));
    }

    @GetMapping
    public ResponseEntity<BoardGame> getGame(@RequestParam(value = "alias", required = false, defaultValue = "") String alias) {
        return ResponseEntity.ok(gameService.findByAlias(alias));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getGames(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                      @RequestParam(value = "sort", required = false) String sortType,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "byUser", required = false, defaultValue = "false") boolean byUser,
                                      Principal p) {
        if (limit < 0 || limit > 100) {
            limit = 10;
        }
        if (offset < 0) {
            offset = 0;
        }

        Sort.Direction sortDirection = null;
        if (!ObjectUtils.isEmpty(sortType)) {
            if (sortType.equalsIgnoreCase("asc".trim())) {
                sortDirection = Sort.Direction.ASC;
            } else if (sortType.equalsIgnoreCase("desc".trim())) {
                sortDirection = Sort.Direction.DESC;
            }
        }

        Sort sort = sortDirection == null ? Sort.unsorted() : Sort.by(sortDirection, "alias");
        Iterable<BoardGame> boardGames;
        if (StringUtils.isNotBlank(title)) {
            boardGames = gameService.getAllGames(limit, offset, sort, StringUtils.trim(title));
        } else {
            boardGames = gameService.getAllGames(limit, offset, sort);
        }

        if (byUser) {
            List<UserBoardGameDto> userBoardGames = new ArrayList<>();
            User user = userService.findByUsername(p.getName());
            boardGames.forEach(boardGame -> {
                userBoardGames.add(new UserBoardGameDto(boardGame, userGameService.findById(user.getId(), boardGame.getId()) != null));
            });
            return ResponseEntity.ok(userBoardGames);
        }

        return ResponseEntity.ok(boardGames);
    }
}
