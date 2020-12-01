package ru.zilzilok.avid.profile.models.entities;

import lombok.*;
import ru.zilzilok.avid.profile.models.enums.Gender;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "hiddenBuilder")
@Entity
@Table(name = "users")
public class User {
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
    private Gender gender;

    @ManyToMany(targetEntity = Role.class,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH} )
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Singular
    private Set<Role> roles;

    public static UserBuilder builder(String username, String password, String email) {
        return hiddenBuilder()
                .username(username)
                .password(password)
                .email(email);
    }
}
