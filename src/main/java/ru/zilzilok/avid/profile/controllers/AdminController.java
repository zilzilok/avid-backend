package ru.zilzilok.avid.profile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zilzilok.avid.profile.models.entities.User;
import ru.zilzilok.avid.profile.repositories.UserRepository;

import java.util.Optional;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepo;

    @Autowired
    public AdminController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable("id") Long id) {
        return userRepo.findById(id);
    }
}
