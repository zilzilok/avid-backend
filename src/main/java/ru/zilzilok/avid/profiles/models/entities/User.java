package ru.zilzilok.avid.profiles.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.zilzilok.avid.profiles.models.enums.Gender;

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

    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String secondName;
    private Date birthdate;
    private String country;
    private String photoPath;

    private boolean active;
    @JsonIgnore
    private String activationCode;

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

    @ManyToMany(
            targetEntity = User.class,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    @JsonIgnore
    @Singular
    @EqualsAndHashCode.Exclude
    private Set<User> friends;

    public void setFirstName(String firstName) {
        if (StringUtils.isNotBlank(firstName)) {
            this.firstName = firstName;
        }
    }

    public void setSecondName(String secondName) {
        if (StringUtils.isNotBlank(secondName)) {
            this.secondName = secondName;
        }
    }

    public void setBirthdate(Date birthdate) {
        if (birthdate != null) {
            this.birthdate = birthdate;
        }
    }

    public void setCountry(String country) {
        if (StringUtils.isNotBlank(country)) {
            this.country = country;
        }
    }

    public void setPhotoPath(String photoPath) {
        if (StringUtils.isNotBlank(photoPath)) {
            this.photoPath = photoPath;
        }
    }

    public void setGender(Gender gender) {
        if (gender != null) {
            this.gender = gender;
        }
    }

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
