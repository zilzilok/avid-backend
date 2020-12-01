package ru.zilzilok.avid.profile.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zilzilok.avid.profile.models.dto.UserRegDto;
import ru.zilzilok.avid.profile.models.entities.Role;
import ru.zilzilok.avid.profile.models.entities.User;
import ru.zilzilok.avid.profile.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean registerNewUserAccount(UserRegDto userRegDto) {
        if (userRepo.findByUsername(userRegDto.getUsername()) == null) {
            String username = userRegDto.getUsername();
            String password = passwordEncoder.encode(userRegDto.getPassword());
            String email = userRegDto.getEmail();
            User user = User.builder(username, password, email)
                    .role(roleService.getRole("USER"))
                    .role(roleService.getRole("ADMIN"))
                    .active(true)
                    .build();

            userRepo.save(user);
            return true;
        }
        return false;
    }
}
