package ru.zilzilok.avid.profile.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.zilzilok.avid.profile.models.dto.UserRegDto;
import ru.zilzilok.avid.profile.models.entities.User;
import ru.zilzilok.avid.profile.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found.");
        }

        return user;
    }

    public User registerNewUserAccount(UserRegDto userRegDto) {
        if (userRepo.findByUsername(userRegDto.getUsername()) == null) {
            String username = userRegDto.getUsername();
            String password = passwordEncoder.encode(userRegDto.getPassword());
            String email = userRegDto.getEmail();
            User user = User.builder(username, password, email)
                    .role(roleService.getOrCreate("ROLE_USER"))
                    .role(roleService.getOrCreate("ROLE_ADMIN"))
                    .active(false)
                    .build();

            return userRepo.save(user);
        }
        return null;
    }
}
