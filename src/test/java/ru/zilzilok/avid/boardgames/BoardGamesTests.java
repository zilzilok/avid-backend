package ru.zilzilok.avid.boardgames;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.zilzilok.avid.boardgames.services.GameService;
import ru.zilzilok.avid.profile.ProfileTestData;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardGamesTests {

    private final static String URL = "/games";

    private final MockMvc mockMvc;
    private final UserService userService;
    private final GameService gameService;
    private User admin;

    @Autowired
    public BoardGamesTests(MockMvc mockMvc, UserService userService, GameService gameService) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.gameService = gameService;
    }

    @BeforeEach
    public void init() {
        admin = userService.findByUsername("test");
        if (admin == null) {
            UserRegDto tmpUser = UserRegDto.builder()
                    .username("test")
                    .password(ProfileTestData.getPassword())
                    .email(ProfileTestData.getEmail())
                    .build();
            userService.registerNewUserAccount(tmpUser);
            admin = userService.findByUsername("test");
        }
    }

    @Test
    public void reloadAllGamesTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/games/reload/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(admin.getUsername()).password(admin.getPassword()).roles("ADMIN")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
