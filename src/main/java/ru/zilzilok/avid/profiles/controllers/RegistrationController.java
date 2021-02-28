package ru.zilzilok.avid.profiles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;
import ru.zilzilok.avid.profiles.models.entities.User;
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
    public ResponseEntity<?> registerUserAccount(@RequestBody @Valid UserRegDto userRegDto) {
        User user = userService.findByUsername(userRegDto.getUsername());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("User with username = '%s' already exists.", userRegDto.getUsername()));
        }
        user = userService.findByEmail(userRegDto.getEmail());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("User with email = '%s' already exists.", userRegDto.getEmail()));
        }

        user = userService.registerNewUserAccount(userRegDto);
        if (user == null) {
            return ResponseEntity.badRequest().body("User cannot be registered.");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<String> activateUser(@PathVariable("code") String code) {
        User user = userService.findByActivationCode(code);
        if (user != null && userService.activateUser(user)) {
            return ResponseEntity.ok("User activated.");
        }
        return ResponseEntity.badRequest().body("User activation failed.");
    }
}

