package ru.zilzilok.avid.profiles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserService;

import java.security.Principal;

@RestController
public class UserClubsController {

    private final UserService userService;

    @Autowired
    public UserClubsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/clubs")
    public ResponseEntity<?> getClubs(Principal p) {
        User user = userService.findByUsername(p.getName());
        return ResponseEntity.ok(user.getClubs());
    }

    @GetMapping("/user/clubs/own")
    public ResponseEntity<?> getOwnClubs(Principal p) {
        User user = userService.findByUsername(p.getName());
        return ResponseEntity.ok(user.getOwnClubs());
    }

    @GetMapping("/user/{id}/clubs")
    public ResponseEntity<?> getUserClubs(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if(user != null) {
            return ResponseEntity.ok(user.getClubs());
        }
        return ResponseEntity.badRequest().body(String.format("User with id = %d doesn't exist.", id));
    }
}
