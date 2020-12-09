package ru.zilzilok.avid.profile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.profile.models.dto.UserInfoDto;
import ru.zilzilok.avid.profile.models.entities.User;
import ru.zilzilok.avid.profile.repositories.UserRepository;
import ru.zilzilok.avid.profile.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepo;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<Set<User>> getUsers(Principal principal) {
        return ResponseEntity.ok(Collections.singleton(userRepo.findByUsername(principal.getName())));
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(Principal p) {
        User user = userRepo.findByUsername(p.getName());
        if (user.getActivationCode() != null) {
            user.setActivationCode(UUID.randomUUID().toString());
            userService.sendActivationMail(user);
            return ResponseEntity.ok("Activation mail has send.");
        }
        return ResponseEntity.ok("User already activated.");
    }

    @PostMapping(value = "/updateinfo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUserInformation(@RequestBody @Valid UserInfoDto userInfoDto, Principal p) {
        User user = userRepo.findByUsername(p.getName());
        user.setFirstName(userInfoDto.getFirstName());
        user.setSecondName(userInfoDto.getSecondName());
        user.setBirthdate(userInfoDto.getBirthdate());
        user.setCountry(userInfoDto.getCountry());
        user.setPhotoPath(userInfoDto.getPhotoPath());
        user.setGender(userInfoDto.getGender());
        return ResponseEntity.ok(userRepo.save(user));
    }

    @GetMapping("/addfriend/{id}")
    public ResponseEntity<String> addFriend(@PathVariable("id") Long id, Principal p) {
        User user = userRepo.findByUsername(p.getName());
        Optional<User> friend = userRepo.findById(id);
        if(friend.isPresent()) {
            if(!user.getId().equals(id)) {
                user.getFriends().add(friend.get());
                return ResponseEntity.ok(String.format("The friend with id = %d added.", id));
            }
            return ResponseEntity.badRequest().body("User can't add yourself to friends list.");
        }
        return ResponseEntity.badRequest().body(String.format("User with id = %d doesn't exist.", id));
    }

    @GetMapping("/addfriend/{username}")
    public ResponseEntity<String> addFriend(@PathVariable("username") String username, Principal p) {
        User user = userRepo.findByUsername(p.getName());
        User friend = userRepo.findByUsername(username);
        if(friend != null) {
            if(!user.getUsername().equals(username)) {
                user.getFriends().add(friend);
                return ResponseEntity.ok(String.format("The friend with username = %s added successfully.", username));
            }
            return ResponseEntity.badRequest().body("User can't add yourself to friends list.");
        }
        return ResponseEntity.badRequest().body(String.format("User with username = %s doesn't exist.", username));
    }
}