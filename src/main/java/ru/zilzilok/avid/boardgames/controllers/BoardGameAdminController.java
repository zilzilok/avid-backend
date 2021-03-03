package ru.zilzilok.avid.boardgames.controllers;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.boardgames.services.GameService;

@RestController
@RequestMapping("/admin")
@Log
public class BoardGameAdminController {

    private final GameService gameService;

    @Autowired
    public BoardGameAdminController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games/reload/all")
    public ResponseEntity<Iterable<BoardGame>> reloadAllGames() {
        log.info("Start reload all board games.");
        gameService.deleteAll();
        log.info("All games have been deleted in db.");
        return ResponseEntity.ok(gameService.addAllFromApi());
    }
}
