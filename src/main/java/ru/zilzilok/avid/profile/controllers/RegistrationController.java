package ru.zilzilok.avid.profile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zilzilok.avid.profile.models.dto.UserRegDto;
import ru.zilzilok.avid.profile.models.entities.User;
import ru.zilzilok.avid.profile.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registerUserAccount(@RequestBody @Valid UserRegDto userRegDto) {
        User user = userService.registerNewUserAccount(userRegDto);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(user);
    }
}

