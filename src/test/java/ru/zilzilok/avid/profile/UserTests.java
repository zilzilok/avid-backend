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
import ru.zilzilok.avid.profiles.models.dto.UserInfoDto;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.models.enums.Gender;
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
    private User testUser;

    private User getTestUser() {
        return userService.findByUsername("test");
    }

    @Autowired
    public UserTests(UserService userService, MockMvc mockMvc) {
        this.userService = userService;
        this.mockMvc = mockMvc;
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
            userService.registerNewUserAccount(tmpUser);
            testUser = userService.findByUsername("test");
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

    @ParameterizedTest
    @MethodSource("validParamsTestData")
    public void getAllWithParamsOkTest(String limit, String offset, String sort) throws Exception {
        String content = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/all?limit=%s&offset=%s&sort=%s", URL, limit, offset, sort))
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

    @ParameterizedTest
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
                Arguments.of(getFirstName(), getSecondName(), getBirthdate(), getCountry(), getPhotoPath(), getGender()),
                Arguments.of("", getSecondName(), getBirthdate(), getCountry(), getPhotoPath(), getGender()),
                Arguments.of(getFirstName(), "", getBirthdate(), getCountry(), getPhotoPath(), getGender()),
                Arguments.of(getFirstName(), getSecondName(), "", getCountry(), getPhotoPath(), getGender()),
                Arguments.of(getFirstName(), getSecondName(), getBirthdate(), "", getPhotoPath(), getGender()),
                Arguments.of(getFirstName(), getSecondName(), getBirthdate(), getCountry(), "", getGender()),
                Arguments.of(getFirstName(), getSecondName(), getBirthdate(), getCountry(), getPhotoPath(), ""),
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

    @ParameterizedTest
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
                MockMvcRequestBuilders.post(URL + "/update/info")
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
}