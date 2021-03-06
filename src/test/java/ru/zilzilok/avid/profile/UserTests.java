package ru.zilzilok.avid.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import ru.zilzilok.avid.profiles.models.dto.UserInfoDto;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.models.enums.Gender;
import ru.zilzilok.avid.profiles.models.other.UserGame;
import ru.zilzilok.avid.profiles.services.UserGameService;
import ru.zilzilok.avid.profiles.services.UserService;

import java.util.stream.Stream;

import static ru.zilzilok.avid.profile.ProfileTestData.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTests {

    private final static String URL = "/user";
    private final static Gson GSON_INSTANCE = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private final MockMvc mockMvc;
    private final UserService userService;
    private final GameService gameService;
    private final UserGameService userGameService;
    private User testUser;

    private User getTestUser() {
        return userService.findByUsername("test");
    }

    @Autowired
    public UserTests(UserService userService, MockMvc mockMvc, GameService gameService, UserGameService userGameService) {
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.gameService = gameService;
        this.userGameService = userGameService;
    }

    @BeforeEach
    public void init() {
        testUser = userService.findByUsername("test");
        if (testUser == null) {
            UserRegDto tmpUser = UserRegDto.builder()
                    .username("test")
                    .password(ProfileTestData.getPassword())
                    .email(ProfileTestData.getEmail())
                    .build();
            testUser = userService.registerNewUserAccount(tmpUser);
        }
    }

    @Test
    public void getByIdTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getByUsernameTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "?username=" + testUser.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

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
                Arguments.of("-1", "-1", "", ""),
                Arguments.of("", "1", "DeSc", ""),
                Arguments.of("", "0", "asc",  "o"),
                Arguments.of("", "0", "asc",  "O"),
                Arguments.of("", "0", "asc",  "H"),
                Arguments.of("", "0", "asc",  "h")
                );
    }

    @ParameterizedTest(name = "#{index} - Run test with limit =''{0}'', offset =''{1}'', sort =''{2}'', startsWith =''{3}''")
    @MethodSource("validParamsTestData")
    public void getAllWithParamsOkTest(String limit, String offset, String sort, String startsWith) throws Exception {
        String content = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/all?limit=%s&offset=%s&sort=%s&startsWith=%s",
                        URL, limit, offset, sort, startsWith))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("count = " + GSON_INSTANCE.fromJson(content, User[].class).length);
    }

    private static Stream<Arguments> invalidParamsTestData() {
        return Stream.of(
                Arguments.of("kek", "0", ""),
                Arguments.of("1", "kek", ""),
                Arguments.of("kek", "kek", "")
        );
    }

    @ParameterizedTest(name = "#{index} - Run test with limit =''{0}'', offset =''{1}'', sort =''{2}''")
    @MethodSource("invalidParamsTestData")
    public void getAllWithParamsBadRequestTest(String limit, String offset, String sort) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/all?limit=%s&offset=%s&sort=%s", URL, limit, offset, sort))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private static Stream<Arguments> updateInfoTestData() {
        return Stream.of(
                Arguments.of(getFirstName(), getSecondName(), getBirthdate(), getCountry(), getPhotoUrl(), getGender()),
                Arguments.of("", getSecondName(), getBirthdate(), getCountry(), getPhotoUrl(), getGender()),
                Arguments.of(getFirstName(), "", getBirthdate(), getCountry(), getPhotoUrl(), getGender()),
                Arguments.of(getFirstName(), getSecondName(), "", getCountry(), getPhotoUrl(), getGender()),
                Arguments.of(getFirstName(), getSecondName(), getBirthdate(), "", getPhotoUrl(), getGender()),
                Arguments.of(getFirstName(), getSecondName(), getBirthdate(), getCountry(), "", getGender()),
                Arguments.of(getFirstName(), getSecondName(), getBirthdate(), getCountry(), getPhotoUrl(), ""),
                Arguments.of("", "", "", "", "", ""),
                Arguments.of(null, null, null, null, null, null)
        );
    }

    private static void assertEqualsOrNotEquals(Object oldValue, Object newValue) {
        if (ObjectUtils.isNotEmpty(newValue)) {
            Assertions.assertEquals(oldValue, newValue);
        } else {
            Assertions.assertNotEquals(oldValue, newValue);
        }

    }

    @ParameterizedTest(name = "#{index} - Run test with firstName =''{0}'', secondName =''{1}'', birthdate =''{2}''," +
            " country =''{3}'', photoPath =''{4}'', gender =''{5}''")
    @MethodSource("updateInfoTestData")
    public void updateInfoTest(String firstName,
                               String secondName,
                               String birthdate,
                               String country,
                               String photoPath,
                               String gender) throws Exception {
        UserInfoDto userInfo = UserInfoDto.builder()
                .firstName(firstName)
                .secondName(secondName)
                .birthdate(birthdate)
                .country(country)
                .photoPath(photoPath)
                .gender(Gender.fromString(gender)).build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(URL + "/info/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON_INSTANCE.toJson(userInfo))
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        User user = getTestUser();

        assertEqualsOrNotEquals(user.getSecondName(), secondName);
        assertEqualsOrNotEquals(user.getSecondName(), secondName);
        assertEqualsOrNotEquals(user.getBirthdate().toString(), birthdate);
        assertEqualsOrNotEquals(user.getCountry(), country);
        assertEqualsOrNotEquals(user.getPhotoPath(), photoPath);
        assertEqualsOrNotEquals(user.getGender(), Gender.fromString(gender));
    }

    @Test
    public void friendsAdditionByIdTest() throws Exception {
        /* add new random user */
        String username = getUsername();
        String password = getPassword();
        UserRegDto newUser = UserRegDto.builder()
                .username(username)
                .password(password)
                .matchingPassword(password)
                .email(getEmail()).build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON_INSTANCE.toJson(newUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        User friend = userService.findByUsername(username);

        /* add new user to friends of testUser */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/friends/add/" + friend.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        /* "testUser can't add yourself to friends list" */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/friends/add/" + testUser.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        friend = userService.findByUsername(username);
        Assertions.assertFalse(friend.getFriends().contains(getTestUser()));
        Assertions.assertTrue(getTestUser().getFriends().contains(friend));

        /* add testUser to friends of new user */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/friends/add/" + testUser.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(friend.getUsername()).password(friend.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        /* "New user can't add yourself to friends list" */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/friends/add/" + friend.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(friend.getUsername()).password(friend.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        friend = userService.findByUsername(username);
        Assertions.assertTrue(friend.getFriends().contains(getTestUser()));
        Assertions.assertTrue(getTestUser().getFriends().contains(friend));

        /* remove new user to friends of testUser */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/friends/remove/" + friend.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        /* "testUser can't remove yourself from friends list" */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/friends/remove/" + testUser.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        friend = userService.findByUsername(username);
        Assertions.assertTrue(friend.getFriends().contains(getTestUser()));
        Assertions.assertFalse(getTestUser().getFriends().contains(friend));

        /* remove testUser to friends of new user */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/friends/remove/" + testUser.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(friend.getUsername()).password(friend.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        /* "New user can't remove yourself from friends list" */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/friends/remove/" + friend.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(friend.getUsername()).password(friend.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        friend = userService.findByUsername(username);
        Assertions.assertFalse(friend.getFriends().contains(getTestUser()));
        Assertions.assertFalse(getTestUser().getFriends().contains(friend));
    }

    @Test
    public void gamesAdditionByIdTest() throws Exception {
        String content = mockMvc.perform(
                MockMvcRequestBuilders.get("/games/all?limit=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        BoardGame[] games = GSON_INSTANCE.fromJson(content, BoardGame[].class);
        Assertions.assertNotEquals(games.length, 0);
        BoardGame game = games[0];

        /* makes sure that the game was initially removed */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/games/remove/" + game.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print());

        /* testUser is adding board game */
        mockMvc.perform(
                MockMvcRequestBuilders.post(URL + "/games/add/" + game.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.post(URL + "/games/add/" + game.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        game = gameService.findById(game.getId());
        testUser = getTestUser();
        UserGame userGame = userGameService.findById(testUser.getId(), game.getId());

        Assertions.assertTrue(game.getOwners().contains(userGame));
        Assertions.assertTrue(testUser.getGames().contains(userGame));

        /* testUser is removing board game */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/games/remove/" + game.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/games/remove/" + game.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        userGame = userGameService.findById(testUser.getId(), game.getId());
        Assertions.assertNull(userGame);
    }

    @Test
    public void gamesAdditionByAliasTest() throws Exception {
        String content = mockMvc.perform(
                MockMvcRequestBuilders.get("/games/all?limit=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        BoardGame[] games = GSON_INSTANCE.fromJson(content, BoardGame[].class);
        Assertions.assertNotEquals(games.length, 0);
        BoardGame game = games[0];
        System.out.println(game.toString());

        /* makes sure that the game was initially removed */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/games/remove?alias=" + game.getAlias())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print());

        /* testUser is adding board game */
        mockMvc.perform(
                MockMvcRequestBuilders.post(URL + "/games/add?alias=" + game.getAlias())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.post(URL + "/games/add?alias=" + game.getAlias())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        game = gameService.findById(game.getId());
        testUser = getTestUser();
        UserGame userGame = userGameService.findById(testUser.getId(), game.getId());

        Assertions.assertTrue(game.getOwners().contains(userGame));
        Assertions.assertTrue(testUser.getGames().contains(userGame));

        /* testUser is removing board game */
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/games/remove?alias=" + game.getAlias())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/games/remove?alias=" + game.getAlias())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        userGame = userGameService.findById(testUser.getId(), game.getId());
        Assertions.assertNull(userGame);
    }
}
