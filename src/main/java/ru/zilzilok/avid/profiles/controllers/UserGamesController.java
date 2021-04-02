package ru.zilzilok.avid.profiles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.boardgames.services.GameService;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserGamesController {

    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public UserGamesController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public ResponseEntity<?> getUserGames(Principal p) {
        User user = userService.findByUsername(p.getName());
        return ResponseEntity.ok(user.getGames());
    }

    @GetMapping("/{id}/games")
    public ResponseEntity<?> getUserGames(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if(user != null) {
            return ResponseEntity.ok(user.getGames());
        }
        return ResponseEntity.badRequest().body(String.format("User with id = %d doesn't exist.", id));
    }

    @GetMapping("/games/add/{id}")
    public ResponseEntity<?> addGame(@PathVariable("id") Long id, Principal p) {
        User user = userService.findByUsername(p.getName());
        BoardGame game = gameService.findById(id);
        if (game != null) {
            if (user.getGames().contains(game)) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s already added board game with id = %d.", p.getName(), id));
            }

            userService.addGame(user, game);
            return ResponseEntity.ok(String.format("The board game with id = %d added successfully.", id));
        }
        return ResponseEntity.badRequest().body(String.format("Board game with id = %d doesn't exist.", id));
    }

    @GetMapping("/games/add")
    public ResponseEntity<?> addGame(@RequestParam("alias") String alias, Principal p) {
        User user = userService.findByUsername(p.getName());
        BoardGame game = gameService.findByAlias(alias);
        if (game != null) {
            if (user.getGames().contains(game)) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s already added board game with alias = %s.", p.getName(), alias));
            }

            userService.addGame(user, game);
            return ResponseEntity.ok(String.format("The board game with alias = %s added successfully.", alias));
        }
        return ResponseEntity.badRequest().body(String.format("Board game with alias = %s doesn't exist.", alias));
    }

    @GetMapping("/games/remove/{id}")
    public ResponseEntity<?> removeGame(@PathVariable("id") Long id, Principal p) {
        User user = userService.findByUsername(p.getName());
        BoardGame game = gameService.findById(id);
        if (game != null) {
            if (!user.getGames().contains(game)) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s doesn't have board game with id = %d.", p.getName(), id));
            }

            userService.removeGame(user, game);
            return ResponseEntity.ok(String.format("The board game with id = %d removed successfully.", id));
        }
        return ResponseEntity.badRequest().body(String.format("Board game with id = %d doesn't exist.", id));
    }

    @GetMapping("/games/remove")
    public ResponseEntity<?> removeGame(@RequestParam("alias") String alias, Principal p) {
        User user = userService.findByUsername(p.getName());
        BoardGame game = gameService.findByAlias(alias);
        if (game != null) {
            if (!user.getGames().contains(game)) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s doesn't have board game with alias = %s.", p.getName(), alias));
            }

            userService.removeGame(user, game);
            return ResponseEntity.ok(String.format("The board game with alias = %s removed successfully.", alias));
        }
        return ResponseEntity.badRequest().body(String.format("Board game with alias = %s doesn't exist.", alias));
    }
}
