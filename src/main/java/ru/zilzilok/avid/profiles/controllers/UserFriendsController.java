package ru.zilzilok.avid.profiles.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/user/friends")
public class UserFriendsController {

    private final UserService userService;

    @Autowired
    public UserFriendsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getFriends(Principal p, @RequestParam(value = "startsWith", required = false) String startsWith) {
        if(StringUtils.isNotBlank(startsWith)) {
            return ResponseEntity.ok(userService.getAllFriends(p.getName(), startsWith));
        }
        User user = userService.findByUsername(p.getName());
        return ResponseEntity.ok(user.getFriends());
    }

    @GetMapping("/add/{id}")
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

    @GetMapping("/add")
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
}
