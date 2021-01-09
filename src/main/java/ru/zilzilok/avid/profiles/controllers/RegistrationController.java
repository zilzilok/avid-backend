package ru.zilzilok.avid.profiles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.repositories.UserRepository;
import ru.zilzilok.avid.profiles.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registerUserAccount(@RequestBody @Valid UserRegDto userRegDto) {
        User user = userService.registerNewUserAccount(userRegDto);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<String> activateUser(@PathVariable("code") String code) {
        User user = userService.findByActivationCode(code);
        if(user != null && userService.activateUser(user)){
            return ResponseEntity.ok("User activated.");
        }
        return ResponseEntity.badRequest().body("User activation failed.");
    }
}

