package ru.zilzilok.avid.profiles.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.zilzilok.avid.profiles.models.dto.UserInfoDto;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.repositories.UserRepository;
import ru.zilzilok.avid.tools.OffsetBasedPageRequest;

import java.util.Optional;
import java.util.UUID;

@Service
@EnableTransactionManagement
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Value("${hostname}")
    private String hostname;

    @Autowired
    public UserService(
            UserRepository userRepo,
            RoleService roleService,
            PasswordEncoder passwordEncoder,
            MailSender mailSender) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found.");
        }

        return user;
    }

    @Transactional
    public User findById(Long id) {
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Transactional
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Transactional
    public User findByActivationCode(String code) {
        return userRepo.findByActivationCode(code);
    }

    @Transactional
    public User findByUsernameAndPassword(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user != null && password != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
            throw new UsernameNotFoundException("Wrong password.");
        }
        throw new UsernameNotFoundException("Username not found.");
    }

    @Transactional
    public Iterable<User> getAll(int limit, int offset, Sort sort) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit, sort);
        return userRepo.findAll(pageable).getContent();
    }


    @Transactional
    public Iterable<User> getAll(int limit, int offset, Sort sort, String startsWith) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit, sort);
        return userRepo.findByNameStartingWithIgnoreCase(startsWith, pageable);
    }

    @Transactional
    public Iterable<User> getAllFriends(String username, String startsWith) {
        return userRepo.findFriendsByNameStartingWithIgnoreCase(username, startsWith);
    }

    @Transactional
    public User registerNewUserAccount(UserRegDto userRegDto) {
        String username = userRegDto.getUsername();
        String email = userRegDto.getEmail();
        if (userRepo.findByUsername(username) == null && userRepo.findByEmail(email) == null) {
            String password = passwordEncoder.encode(userRegDto.getPassword());
            User user = User.builder(username, password, email)
                    .role(roleService.getOrCreate("ROLE_USER"))
                    .active(false)
                    .build();

            user.setActivationCode(UUID.randomUUID().toString());
            sendActivationMail(user);

            return userRepo.save(user);
        }
        return null;
    }

    @Transactional
    public void updateInformation(User user, UserInfoDto userInfoDto) {
        user.setFirstName(userInfoDto.getFirstName());
        user.setSecondName(userInfoDto.getSecondName());
        user.setBirthdate(userInfoDto.getBirthdate());
        user.setCountry(userInfoDto.getCountry());
        user.setPhotoPath(userInfoDto.getPhotoPath());
        user.setGender(userInfoDto.getGender());
        userRepo.save(user);
    }

    @Transactional
    public void sendActivationMail(User user) {
        String email = user.getEmail();
        if (StringUtils.isNotBlank(email)) {
            String message = String.format("Привет, %s!\n" +
                            "Добро пожаловать в Avid.\n" +
                            "Для активации зайдите на: %s/registration/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );
            mailSender.send(email, "Код активации", message);
        }
    }

    @Transactional
    public boolean activateUser(User user) {
        if (user == null) {
            return false;
        }

        user.setActive(true);
        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }

    @Transactional
    public void addFriend(User user, User friend) {
        user.getFriends().add(friend);
    }
}
