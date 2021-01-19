package ru.zilzilok.avid.сlubs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserService;
import ru.zilzilok.avid.сlubs.models.dto.ClubDto;
import ru.zilzilok.avid.сlubs.models.entities.Club;
import ru.zilzilok.avid.сlubs.services.ClubService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;
    private final UserService userService;

    @Autowired
    public ClubController(ClubService clubService, UserService userService) {
        this.clubService = clubService;
        this.userService = userService;
    }

    @PostMapping(name = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createClub(@RequestBody @Valid ClubDto clubDto, Principal p) {
        Club club = clubService.findByName(clubDto.getName());
        if (club == null) {
            User creator = userService.findByUsername(p.getName());
            return ResponseEntity.ok(clubService.createNewClub(clubDto, creator));
        }
        return ResponseEntity.badRequest().body(String.format("Club with name = %s already exists.", clubDto.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClub(@PathVariable("id") Long id) {
        Club club = clubService.findById(id);
        if (club != null) {
            return ResponseEntity.ok(club);
        }
        return ResponseEntity.badRequest().body(String.format("User with id = %d not found.", id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getClubs(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
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

        Sort sort = sortDirection == null ? Sort.unsorted() : Sort.by(sortDirection, "name");
        return ResponseEntity.ok(clubService.getAll(limit, offset, sort));
    }

    @GetMapping("/{id}/join")
    public ResponseEntity<?> joinClub(@PathVariable("id") Long id, Principal p) {
        Club club = clubService.findById(id);
        User user = userService.findByUsername(p.getName());
        if (club != null) {
            if (user.getClubs().contains(club)) {
                return ResponseEntity.badRequest().body(String.format("User %s already joined the club with id = %d", user.getUsername(), club.getId()));
            }
            userService.joinClub(user, club);
            return ResponseEntity.ok(String.format("User %s joined to club with id = %d successfully.", user.getUsername(), club.getId()));
        }
        return ResponseEntity.badRequest().body(String.format("User with id = %d not found.", id));
    }
}
