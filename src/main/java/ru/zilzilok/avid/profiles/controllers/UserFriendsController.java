package ru.zilzilok.avid.profiles.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.profiles.models.dto.UserGameHasDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.models.other.UserGame;
import ru.zilzilok.avid.profiles.services.UserGameService;
import ru.zilzilok.avid.profiles.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserFriendsController {

    private final UserService userService;
    private final UserGameService userGameService;

    @Autowired
    public UserFriendsController(UserService userService, UserGameService userGameService) {
        this.userService = userService;
        this.userGameService = userGameService;
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(Principal p, @RequestParam(value = "startsWith", required = false) String startsWith) {
        if (StringUtils.isNotBlank(startsWith)) {
            return ResponseEntity.ok(userService.getAllFriends(p.getName(), startsWith));
        }
        User user = userService.findByUsername(p.getName());
        return ResponseEntity.ok(user.getFriends());
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<?> getFriends(@PathVariable("id") Long id, @RequestParam(value = "startsWith", required = false) String startsWith) {
        User user = userService.findById(id);
        if (user != null) {
            if (StringUtils.isNotBlank(startsWith)) {
                return ResponseEntity.ok(userService.getAllFriends(user.getUsername(), startsWith));
            }
            return ResponseEntity.ok(user.getFriends());
        }
        return ResponseEntity.badRequest().body(String.format("User with id = %d doesn't exist.", id));
    }

    @GetMapping("/friends/add/{id}")
    public ResponseEntity<String> addFriend(@PathVariable("id") Long id, Principal p) {
        User user = userService.findByUsername(p.getName());
        User friend = userService.findById(id);
        if (friend != null) {
            if (user.getFriends().contains(friend)) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s already added user with id = %d.", p.getName(), id));
            }

            if (!user.getId().equals(id)) {
                userService.addFriend(user, friend);
                if (friend.getFriends().contains(user)) {
                    return ResponseEntity.ok(String.format("The friend with id = %d added successfully.", id));
                }
                return ResponseEntity.ok(String.format("Friend request with user with id = %d sent successfully.", id));
            }
            return ResponseEntity.badRequest().body("User can't add yourself to friends list.");
        }
        return ResponseEntity.badRequest().body(String.format("User with id = %d doesn't exist.", id));
    }

    @GetMapping("/friends/add")
    public ResponseEntity<String> addFriend(@RequestParam("username") String username, Principal p) {
        User user = userService.findByUsername(p.getName());
        User friend = userService.findByUsername(username);
        if (friend != null) {
            if (user.getFriends().contains(friend)) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s already added user with username = %s.", p.getName(), username));
            }

            if (!user.getUsername().equals(username)) {
                userService.addFriend(user, friend);
                if (friend.getFriends().contains(user)) {
                    return ResponseEntity.ok(String.format("The friend with username = %s added successfully.", username));
                }
                return ResponseEntity
                        .ok(String.format("Friend request with user with username = %s sent successfully.", username));
            }
            return ResponseEntity.badRequest().body("User can't add yourself to friends list.");
        }
        return ResponseEntity.badRequest().body(String.format("User with username = %s doesn't exist.", username));
    }

    @GetMapping("/friends/remove/{id}")
    public ResponseEntity<String> removeFriend(@PathVariable("id") Long id, Principal p) {
        User user = userService.findByUsername(p.getName());
        User friend = userService.findById(id);
        if (friend != null) {
            if (!user.getFriends().contains(friend)) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s already removed user with id = %d.", p.getName(), id));
            }

            if (!user.getId().equals(id)) {
                userService.removeFriend(user, friend);
                return ResponseEntity.ok(String.format("The friend with id = %d removed successfully.", id));
            }
            return ResponseEntity.badRequest().body("User can't remove yourself from friends list.");
        }
        return ResponseEntity.badRequest().body(String.format("User with id = %d doesn't exist.", id));
    }

    @GetMapping("/friends/remove")
    public ResponseEntity<String> removeFriend(@RequestParam("username") String username, Principal p) {
        User user = userService.findByUsername(p.getName());
        User friend = userService.findByUsername(username);
        if (friend != null) {
            if (!user.getFriends().contains(friend)) {
                return ResponseEntity.badRequest()
                        .body(String.format("%s already added user with username = %s.", p.getName(), username));
            }

            if (!user.getUsername().equals(username)) {
                userService.removeFriend(user, friend);
                return ResponseEntity.ok(String.format("The friend with username = %s removed successfully.", username));
            }
            return ResponseEntity.badRequest().body("User can't remove yourself from friends list.");
        }
        return ResponseEntity.badRequest().body(String.format("User with username = %s doesn't exist.", username));
    }

    @GetMapping("/friends/games")
    public ResponseEntity<?> getFriendsGames(Principal p,
                                             @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                             @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                             @RequestParam(value = "sort", required = false) String sortType,
                                             @RequestParam(value = "byUser", required = false, defaultValue = "false") boolean byUser) {
        if (limit < 0 || limit > 100) {
            limit = 10;
        }
        if (offset < 0) {
            offset = 0;
        }

        User user = userService.findByUsername(p.getName());

        Iterable<UserGame> userFriendsGames = userGameService.addAverageRating(
                userGameService.findFriendsGames(user.getId(), limit, offset, sortType));

        if (byUser) {
            List<UserGameHasDto> userFriendsGamesHas = new ArrayList<>();
            userFriendsGames.forEach(userGame -> userFriendsGamesHas.add(new UserGameHasDto(userGame,
                    userGameService.findById(user.getId(), userGame.getId().getGameId()) != null)));
            return ResponseEntity.ok(userFriendsGamesHas);
        }

        return ResponseEntity.ok((userFriendsGames));
    }
}
