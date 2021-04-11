package ru.zilzilok.avid.profiles.controllers;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.StorageService;
import ru.zilzilok.avid.profiles.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/user/storage")
public class StorageController {
    private final StorageService storageService;
    private final UserService userService;

    @Autowired
    StorageController(StorageService storageService, UserService userService) {
        this.storageService = storageService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestPart(value = "file") MultipartFile file) {
        if (ObjectUtils.isNotEmpty(file)) {
            return ResponseEntity.ok(storageService.uploadFile(file));
        }
        return ResponseEntity.badRequest().body("File can't be upload.");
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestPart(value = "file") MultipartFile file, Principal p) {
        User user = userService.findByUsername(p.getName());
        if (ObjectUtils.isNotEmpty(file)) {
            if(StringUtils.isNotBlank(user.getPhotoPath())) {
                storageService.deleteFileFromS3Bucket(user.getPhotoPath());
            }
            return ResponseEntity.ok(storageService.uploadFile(file));
        }
        return ResponseEntity.badRequest().body("File can't be update.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam(value = "url") String fileUrl) {
        if(StringUtils.isNotBlank(fileUrl)) {
            storageService.deleteFileFromS3Bucket(fileUrl);
            return ResponseEntity.ok("Successfully deleted.");
        }
        return ResponseEntity.badRequest().body("Wrong URL = " + fileUrl);
    }
}
