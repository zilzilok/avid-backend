package ru.zilzilok.avid.profile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zilzilok.avid.profile.models.entities.User;
import ru.zilzilok.avid.profile.repositories.UserRepository;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

@RestController
public class UserController {
    private final UserRepository userRepo;

    @Autowired
    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/users/**")
    public ResponseEntity<Set<User>> getUsers(Principal principal) {
        return ResponseEntity.ok(Collections.singleton(userRepo.findByUsername(principal.getName())));
    }
}
