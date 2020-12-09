package ru.zilzilok.avid.profile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zilzilok.avid.profile.models.entities.User;
import ru.zilzilok.avid.profile.repositories.UserRepository;
import ru.zilzilok.avid.profile.services.UserService;

import java.security.Principal;
import java.util.Collections;
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
}
