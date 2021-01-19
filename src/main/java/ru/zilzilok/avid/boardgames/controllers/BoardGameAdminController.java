package ru.zilzilok.avid.boardgames.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.boardgames.services.GameService;

@RestController
@RequestMapping("/admin")
public class BoardGameAdminController {

    private final GameService gameService;

    @Autowired
    public BoardGameAdminController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games/reload/all")
    public ResponseEntity<Iterable<BoardGame>> reloadAllGames() {
        gameService.deleteAll();
        return ResponseEntity.ok(gameService.addAllFromApi());
    }
}
