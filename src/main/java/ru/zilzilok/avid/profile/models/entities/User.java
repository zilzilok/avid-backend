package ru.zilzilok.avid.profile.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.zilzilok.avid.profile.models.enums.Gender;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "hiddenBuilder")
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String secondName;
    private Date birthdate;
    private String country;
    private String photoPath;
    private boolean active;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Exclude
    private Gender gender;

    @ManyToMany(
            targetEntity = Role.class,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    @Singular
    @EqualsAndHashCode.Exclude
    private Set<Role> roles;

    public static UserBuilder builder(String username, String password, String email) {
        return hiddenBuilder()
                .username(username)
                .password(password)
                .email(email);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
