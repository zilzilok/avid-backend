package ru.zilzilok.avid.profile;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.services.UserService;

import java.util.UUID;
import java.util.stream.Stream;

import static ru.zilzilok.avid.TestData.*;
import static ru.zilzilok.avid.TestData.getPassword;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationTests {

    private final static String URL = "/registration";
    private final static Gson GSON_INSTANCE = new Gson();

    private final MockMvc mockMvc;
    private final UserService userService;

    @Autowired
    public RegistrationTests(UserService userService, MockMvc mockMvc) {
        this.userService = userService;
        this.mockMvc = mockMvc;
    }

    private static Stream<Arguments> usersTestData() {
        return Stream.of(
                /* Correct user */
                Arguments.of(getUsername(),
                        getPassword(),
                        getEmail(),
                        MockMvcResultMatchers.status().isOk()),
                /* null username */
                Arguments.of(null,
                        getPassword(),
                        getEmail(),
                        MockMvcResultMatchers.status().isBadRequest()),
                /* null passwords */
                Arguments.of(getUsername(),
                        null,
                        getEmail(),
                        MockMvcResultMatchers.status().isBadRequest()),
                /* null email */
                Arguments.of(getUsername(),
                        getPassword(),
                        null,
                        MockMvcResultMatchers.status().isBadRequest()),
                /* empty username */
                Arguments.of("",
                        getPassword(),
                        getEmail(),
                        MockMvcResultMatchers.status().isBadRequest()),
                /* empty passwords */
                Arguments.of(getUsername(),
                        "",
                        getEmail(),
                        MockMvcResultMatchers.status().isBadRequest()),
                /* empty email */
                Arguments.of(getUsername(),
                        getPassword(),
                        "",
                        MockMvcResultMatchers.status().isBadRequest()),
                /* invalid email */
                Arguments.of(getUsername(),
                        getPassword(),
                        "kekEmail",
                        MockMvcResultMatchers.status().isBadRequest())
        );
    }

    @ParameterizedTest
    @MethodSource("usersTestData")
    public void newUserTest(String username, String password, String email, ResultMatcher status) throws Exception {
        UserRegDto newUser = UserRegDto.builder()
                .username(username)
                .password(password)
                .matchingPassword(password)
                .email(email).build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON_INSTANCE.toJson(newUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status);

        if (status == MockMvcResultMatchers.status().isOk()) {
            User user = userService.findByUsername(username);
            Assertions.assertNotNull(user);
        }
    }

    @Test
    public void passwordMismatchTest() throws Exception {
        String password = getPassword();
        UserRegDto newUser = UserRegDto.builder()
                .username(getUsername())
                .password(password)
                .matchingPassword("")
                .email(getEmail()).build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON_INSTANCE.toJson(newUser)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void userActivationTest() throws Exception {
        String username = getUsername();
        String password = getPassword();
        UserRegDto newUser = UserRegDto.builder()
                .username(username)
                .password(password)
                .matchingPassword(password)
                .email(getEmail()).build();

        mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON_INSTANCE.toJson(newUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        User user = userService.findByUsername(username);
        Assertions.assertNotNull(user);
        Assertions.assertFalse(user.isActive());
        Assertions.assertNotNull(user.getActivationCode());

        String activationCode = user.getActivationCode();
        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/activate/" + activationCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        user = userService.findByUsername(username);
        Assertions.assertTrue(user.isActive());
        Assertions.assertNull(user.getActivationCode());

        mockMvc.perform(
                MockMvcRequestBuilders.get(URL + "/activate/" + activationCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
