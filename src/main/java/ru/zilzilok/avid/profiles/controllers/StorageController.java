package ru.zilzilok.avid.profiles.controllers;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.zilzilok.avid.profiles.services.StorageService;

@RestController
@RequestMapping("/user/storage")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestPart(value = "file") MultipartFile file) {
        if (ObjectUtils.isNotEmpty(file)) {
            return ResponseEntity.ok(storageService.uploadFile(file));
        }
        return ResponseEntity.badRequest().body("File can't be upload.");
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
