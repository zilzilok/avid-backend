package ru.zilzilok.avid.profiles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.boardgames.services.GameService;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.models.other.UserGame;
import ru.zilzilok.avid.profiles.services.UserGameService;
import ru.zilzilok.avid.profiles.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserGamesController {

    private final UserService userService;
    private final GameService gameService;
    private final UserGameService userGameService;

    @Autowired
    public UserGamesController(UserService userService, GameService gameService, UserGameService userGameService) {
        this.userService = userService;
        this.gameService = gameService;
        this.userGameService = userGameService;
    }

    @GetMapping("/games")
    public ResponseEntity<?> getUserGames(Principal p) {
        User user = userService.findByUsername(p.getName());
        return ResponseEntity.ok(user.getGames());
    }

    @GetMapping("/{id}/games")
    public ResponseEntity<?> getUserGames(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user.getGames());
        }
        return ResponseEntity.badRequest().body(String.format("User with id = %d doesn't exist.", id));
    }

    @PostMapping("/games/add/{id}")
    public ResponseEntity<?> addGame(@PathVariable("id") Long id,
                                     @RequestParam(value = "review", required = false, defaultValue = "") String review,
                                     @RequestParam(value = "rating", required = false, defaultValue = "5") double rating,
                                     Principal p) {
        BoardGame game = gameService.findById(id);
        if (game != null) {
            User user = userService.findByUsername(p.getName());
            UserGame userGame = userGameService.findById(user.getId(), id);
            if (userGame != null) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s already added board game with id = %d.", p.getName(), id));
            }
            userGameService.addGame(user, game, review, rating);
            return ResponseEntity.ok(String.format("The board game with id = %d added successfully.", id));
        }
        return ResponseEntity.badRequest().body(String.format("Board game with id = %d doesn't exist.", id));
    }

    @PostMapping("/games/add")
    public ResponseEntity<?> addGame(@RequestParam("alias") String alias,
                                     @RequestParam(value = "review", required = false, defaultValue = "") String review,
                                     @RequestParam(value = "rating", required = false, defaultValue = "5") double rating,
                                     Principal p) {
        BoardGame game = gameService.findByAlias(alias);
        if (game != null) {
            User user = userService.findByUsername(p.getName());
            UserGame userGame = userGameService.findById(user.getId(), game.getId());
            if (userGame != null) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s already added board game with alias = %s.", p.getName(), alias));
            }

            userGameService.addGame(user, game, review, rating);
            return ResponseEntity.ok(String.format("The board game with alias = %s added successfully.", alias));
        }
        return ResponseEntity.badRequest().body(String.format("Board game with alias = %s doesn't exist.", alias));
    }

    @GetMapping("/games/remove/{id}")
    public ResponseEntity<?> removeGame(@PathVariable("id") Long id, Principal p) {
        BoardGame game = gameService.findById(id);
        if (game != null) {
            User user = userService.findByUsername(p.getName());
            UserGame userGame = userGameService.findById(user.getId(), game.getId());
            if (userGame == null) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s doesn't have board game with id = %d.", p.getName(), id));
            }

            userGameService.removeGame(user, game);
            return ResponseEntity.ok(String.format("The board game with id = %d removed successfully.", id));
        }
        return ResponseEntity.badRequest().body(String.format("Board game with id = %d doesn't exist.", id));
    }

    @GetMapping("/games/remove")
    public ResponseEntity<?> removeGame(@RequestParam("alias") String alias, Principal p) {
        BoardGame game = gameService.findByAlias(alias);
        if (game != null) {
            User user = userService.findByUsername(p.getName());
            UserGame userGame = userGameService.findById(user.getId(), game.getId());
            if (userGame == null) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s doesn't have board game with alias = %s.", p.getName(), alias));
            }

            userGameService.removeGame(user, game);
            return ResponseEntity.ok(String.format("The board game with alias = %s removed successfully.", alias));
        }
        return ResponseEntity.badRequest().body(String.format("Board game with alias = %s doesn't exist.", alias));
    }
}
