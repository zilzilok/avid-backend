package ru.zilzilok.avid.profiles.models.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}