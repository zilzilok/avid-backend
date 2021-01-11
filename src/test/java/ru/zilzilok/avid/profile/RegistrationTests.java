package ru.zilzilok.avid.profile;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationTests {

    private final static String URL = "/registration";
    private final static Gson GSON_INSTANCE = new Gson();

    @Autowired
    private MockMvc mockMvc;

    private static Stream<Arguments> usersTestData() {
        return Stream.of(
                /* Correct user */
                Arguments.of(UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        String.format("%d@test.ru", System.currentTimeMillis()),
                        MockMvcResultMatchers.status().isOk()),
                /* null username */
                Arguments.of(null,
                        UUID.randomUUID().toString(),
                        String.format("%d@test.ru", System.currentTimeMillis()),
                        MockMvcResultMatchers.status().isBadRequest()),
                /* null passwords */
                Arguments.of(UUID.randomUUID().toString(),
                        null,
                        String.format("%d@test.ru", System.currentTimeMillis()),
                        MockMvcResultMatchers.status().isBadRequest()),
                /* null email */
                Arguments.of(UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        null,
                        MockMvcResultMatchers.status().isBadRequest()),
                /* empty username */
                Arguments.of("",
                        UUID.randomUUID().toString(),
                        String.format("%d@test.ru", System.currentTimeMillis()),
                        MockMvcResultMatchers.status().isBadRequest()),
                /* empty passwords */
                Arguments.of(UUID.randomUUID().toString(),
                        "",
                        String.format("%d@test.ru", System.currentTimeMillis()),
                        MockMvcResultMatchers.status().isBadRequest()),
                /* empty email */
                Arguments.of(UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        "",
                        MockMvcResultMatchers.status().isBadRequest()),
                /* invalid email */
                Arguments.of(UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
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

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON_INSTANCE.toJson(newUser)))
                .andExpect(status);
    }

    @Test
    public void passwordMismatchTest() throws Exception {
        String password = UUID.randomUUID().toString();
        UserRegDto newUser = UserRegDto.builder()
                .username(UUID.randomUUID().toString())
                .password(password)
                .matchingPassword("")
                .email(String.format("%d@test.ru", System.currentTimeMillis())).build();

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GSON_INSTANCE.toJson(newUser)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
