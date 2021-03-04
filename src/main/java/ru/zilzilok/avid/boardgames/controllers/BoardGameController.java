package ru.zilzilok.avid.boardgames.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.boardgames.services.GameService;

@RestController
@RequestMapping("/games")
public class BoardGameController {

    private final GameService gameService;

    @Autowired
    public BoardGameController(GameService gameService) {
        this.gameService = gameService;
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
    public ResponseEntity<Iterable<BoardGame>> getGames(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                                        @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                        @RequestParam(value = "sort", required = false) String sortType) {
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
        return ResponseEntity.ok(gameService.getAllGames(limit, offset, sort));
    }
}
