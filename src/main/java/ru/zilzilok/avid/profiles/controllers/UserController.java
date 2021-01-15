package ru.zilzilok.avid.profiles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.profiles.models.dto.UserInfoDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUsers(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<User> getUsers(@RequestParam(name = "username", required = false) String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<User>> getUsers(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                                   @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                   @RequestParam(value = "sort", required = false) String sortType) {
        if (limit < 1 || limit > 100) {
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

        Sort sort = sortDirection == null ? Sort.unsorted() : Sort.by(sortDirection, "username");
        return ResponseEntity.ok(userService.getAll(limit, offset, sort));
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(Principal p) {
        User user = userService.findByUsername(p.getName());
        if (user.getActivationCode() != null) {
            user.setActivationCode(UUID.randomUUID().toString());
            userService.sendActivationMail(user);
            return ResponseEntity.ok("Activation mail has send.");
        }
        return ResponseEntity.ok("User already activated.");
    }

    @PostMapping(value = "/update/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUserInformation(@RequestBody @Valid UserInfoDto userInfoDto, Principal p) {
        User user = userService.findByUsername(p.getName());
        userService.updateInformation(user, userInfoDto);
        return ResponseEntity.ok(user);
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
                user.getFriends().add(friend);
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
                user.getFriends().add(friend);
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