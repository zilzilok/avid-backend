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
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<User> getUser(@RequestParam(name = "username", required = false) String username, Principal p) {
        if (username == null) {
            return ResponseEntity.ok(userService.findByUsername(p.getName()));
        }
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

    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo(Principal p) {
        return ResponseEntity.ok(userService.findByUsername(p.getName()));
    }


    @PostMapping(value = "/info/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUserInfo(@RequestBody @Valid UserInfoDto userInfoDto, Principal p) {
        User user = userService.findByUsername(p.getName());
        userService.updateInformation(user, userInfoDto);
        return ResponseEntity.ok(user);
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
}