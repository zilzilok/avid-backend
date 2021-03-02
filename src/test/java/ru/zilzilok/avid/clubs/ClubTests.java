package ru.zilzilok.avid.clubs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserService;
import ru.zilzilok.avid.сlubs.models.dto.ClubDto;
import ru.zilzilok.avid.сlubs.models.entities.Club;
import ru.zilzilok.avid.сlubs.services.ClubService;

import java.util.stream.Stream;

import static ru.zilzilok.avid.clubs.ClubTestData.*;
import static ru.zilzilok.avid.profile.ProfileTestData.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClubTests {
    private final static String URL = "/clubs";
    private final static Gson GSON_INSTANCE = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private final MockMvc mockMvc;
    private final UserService userService;
    private final ClubService clubService;
    private User testUser;
    private Club testClub;

    private User getTestUser() {
        return userService.findByUsername("test");
    }

    private Club getTestClub() {
        return clubService.findByName("testClub");
    }

    @Autowired
    public ClubTests(UserService userService, ClubService clubService, MockMvc mockMvc) {
        this.userService = userService;
        this.clubService = clubService;
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void init() {
        testUser = userService.findByUsername("test");
        if (testUser == null) {
            UserRegDto tmpUser = UserRegDto.builder()
                    .username("test")
                    .password(getPassword())
                    .email(getEmail())
                    .build();
            testUser = userService.registerNewUserAccount(tmpUser);
        }
        testClub = clubService.findByName("testClub");
        if (testClub == null) {
            ClubDto tmpClub = ClubDto.builder()
                    .name("testClub")
                    .description(getDescription())
                    .descriptionShort(getDescriptionShort())
                    .photoUrl(ClubTestData.getPhotoUrl())
                    .build();
            testClub = clubService.createNewClub(tmpClub, testUser);
        }
    }

    @Test
    public void createClubTest() throws Exception {
        String name = getName();
        ClubDto clubDto = ClubDto.builder()
                .name(name)
                .description(getDescription())
                .descriptionShort(getDescriptionShort())
                .photoUrl(ClubTestData.getPhotoUrl())
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON_INSTANCE.toJson(clubDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON_INSTANCE.toJson(clubDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Club club = clubService.findByName(name);
        Assertions.assertNotNull(club);
        Assertions.assertEquals(club.getCreator(), testUser);
        Assertions.assertTrue(club.getMembers().contains(testUser));
        Assertions.assertTrue(getTestUser().getClubs().contains(club));
        Assertions.assertTrue(getTestUser().getOwnClubs().contains(club));
    }

    @Test
    public void getByIdTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/" + testClub.getId())
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
                Arguments.of("1", "0", ""),
                Arguments.of("10", "0", ""),
                Arguments.of("10", "0", "asc"),
                Arguments.of("10", "0", "desc"),
                Arguments.of("3", "2", ""),
                Arguments.of("3", "2", "ASC"),
                Arguments.of("3", "2", "DESC"),
                Arguments.of("3", "1", "aSc"),
                Arguments.of("3", "1", "AsC"),
                Arguments.of("3", "1", "DeSc"),
                Arguments.of("-1", "-1", "")
        );
    }

    @ParameterizedTest(name = "#{index} - Run test with limit =''{0}'', offset =''{1}'', sort =''{2}''")
    @MethodSource("validParamsTestData")
    public void getAllWithParamsOkTest(String limit, String offset, String sort) throws Exception {
        String content = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/all?limit=%s&offset=%s&sort=%s", URL, limit, offset, sort))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("count = " + GSON_INSTANCE.fromJson(content, Club[].class).length);
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

    @Test
    public void joinClubTest() throws Exception {
        /* Create correct user */
        String password = getPassword();
        String username = getUsername();

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

        User user = userService.findByUsername(username);
        Assertions.assertNotNull(user);

        /* join the test club */
        mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/%d/join", URL, testClub.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername()).password(user.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        user = userService.findByUsername(username);

        Assertions.assertTrue(getTestClub().getMembers().contains(user));
        Assertions.assertTrue(user.getClubs().contains(getTestClub()));

        /* join the test club second time */
        mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/%d/join", URL, testClub.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername()).password(user.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        /* join the own club */
        mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/%d/join", URL, testClub.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUsername()).password(testUser.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Assertions.assertNotEquals(user, getTestClub().getCreator());
        Assertions.assertFalse(user.getOwnClubs().contains(testClub));
    }

    @Test
    public void leaveClubTest() throws Exception {
        /* Create correct user */
        String password = getPassword();
        String username = getUsername();

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

        User user = userService.findByUsername(username);
        Assertions.assertNotNull(user);

        /* leave the test club (without joining it) */
        mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/%d/leave", URL, testClub.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername()).password(user.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        /* join the test club */
        mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/%d/join", URL, testClub.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername()).password(user.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        user = userService.findByUsername(username);
        Assertions.assertTrue(getTestClub().getMembers().contains(user));
        Assertions.assertTrue(user.getClubs().contains(getTestClub()));

        /* leave the test club */
        mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/%d/leave", URL, testClub.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername()).password(user.getPassword())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        user = userService.findByUsername(username);
        Assertions.assertFalse(getTestClub().getMembers().contains(user));
        Assertions.assertFalse(user.getClubs().contains(getTestClub()));
    }
}
