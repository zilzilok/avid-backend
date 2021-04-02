package ru.zilzilok.avid.boardgames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.boardgames.services.GameService;
import ru.zilzilok.avid.profile.ProfileTestData;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserService;

import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardGamesTests {

    private final static String URL = "/games";

    private final MockMvc mockMvc;
    private final UserService userService;
    private final GameService gameService;
    private User admin;
    private final static Gson GSON_INSTANCE = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

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
            admin = userService.registerNewUserAccount(tmpUser);
        }
    }

    // RELOAD DB TEST
//    @Test
//    public void reloadAllGamesTest() throws Exception {
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/admin/games/reload/all")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(SecurityMockMvcRequestPostProcessors.user(admin.getUsername()).password(admin.getPassword()).roles("ADMIN")))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void reloadAllGamesWithLimitTest() throws Exception {
//        int limit = 100;
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/admin/games/reload/all/" + limit)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(SecurityMockMvcRequestPostProcessors.user(admin.getUsername()).password(admin.getPassword()).roles("ADMIN")))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        Assertions.assertEquals(limit, gameService.count());
//    }
//
//    @Test
//    public void reloadAllGamesWithLimitByRatingTest() throws Exception {
//        int limit = 1000;
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/admin/games/reload/all/" + limit + "/bgg-rating")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(SecurityMockMvcRequestPostProcessors.user(admin.getUsername()).password(admin.getPassword()).roles("ADMIN")))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        Assertions.assertEquals(limit, gameService.count());
//    }

    private static Stream<Arguments> validParamsTestData() {
        return Stream.of(
                Arguments.of("1", "0", "", ""),
                Arguments.of("10", "0", "", ""),
                Arguments.of("10", "0", "asc", ""),
                Arguments.of("10", "0", "desc", ""),
                Arguments.of("3", "2", "", ""),
                Arguments.of("3", "2", "ASC", ""),
                Arguments.of("3", "2", "DESC", ""),
                Arguments.of("3", "1", "aSc", ""),
                Arguments.of("3", "1", "AsC", ""),
                Arguments.of("3", "1", "DeSc", ""),
                Arguments.of("-1", "-1", "", ""),
                Arguments.of("", "0", "asc",  "G"),
                Arguments.of("", "0", "asc",  "a"),
                Arguments.of("", "0", "asc",  "пан"),
                Arguments.of("", "0", "",  "пир"),
                Arguments.of("", "0", "",  "клан")
        );
    }

    @ParameterizedTest(name = "#{index} - Run test with limit =''{0}'', offset =''{1}'', sort =''{2}'', title =''{3}''")
    @MethodSource("validParamsTestData")
    public void getAllWithParamsOkTest(String limit, String offset, String sort, String title) throws Exception {
        String content = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/all?limit=%s&offset=%s&sort=%s&title=%s",
                        URL, limit, offset, sort, title))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(admin.getUsername()).password(admin.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("count = " + GSON_INSTANCE.fromJson(content, BoardGame[].class).length);
    }
}
