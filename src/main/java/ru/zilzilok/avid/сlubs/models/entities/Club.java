package ru.zilzilok.avid.сlubs.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.zilzilok.avid.profiles.models.entities.User;

import javax.persistence.*;
import java.util.Set;

@Builder(builderMethodName = "hiddenBuilder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clubs")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String description;
    private String descriptionShort;
    private String photoUrl;
    private String alias;

    @ManyToOne(targetEntity = User.class,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "creator_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User creator;

    @ManyToMany(
            targetEntity = User.class, mappedBy = "clubs",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    @Singular
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> members;

    public static Club.ClubBuilder builder(String name) {
        return hiddenBuilder()
                .name(name);
    }
}
