package ru.zilzilok.avid.profile;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.zilzilok.avid.profiles.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTests {

    private final static String URL = "/registration";
    private final static Gson GSON_INSTANCE = new Gson();

    private final MockMvc mockMvc;
    private final UserService userService;

    @Autowired
    public UserTests(UserService userService, MockMvc mockMvc) {
        this.userService = userService;
        this.mockMvc = mockMvc;
    }
}
