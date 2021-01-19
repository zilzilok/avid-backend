package ru.zilzilok.avid.clubs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.zilzilok.avid.profiles.services.UserService;
import ru.zilzilok.avid.сlubs.services.ClubService;

@SpringBootTest
@AutoConfigureMockMvc
public class ClubTests {
    private final static String URL = "/clubs";

    private final MockMvc mockMvc;
    private final UserService userService;
    private final ClubService clubService;

    @Autowired
    public ClubTests(UserService userService, ClubService clubService, MockMvc mockMvc) {
        this.userService = userService;
        this.clubService = clubService;
        this.mockMvc = mockMvc;
    }
}
