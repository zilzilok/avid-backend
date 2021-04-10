package ru.zilzilok.avid.сlubs.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
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
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    private String name;
    private String description;
    private String descriptionShort;
    private String photoUrl;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creator_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User creator;

    @ManyToMany(
            targetEntity = User.class, mappedBy = "clubs",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )
    @JsonIgnore
    @Singular
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> members;

    public void setName(String name) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
    }

    public void setDescription(String description) {
        if (StringUtils.isNotBlank(description)) {
            this.description = description;
        }
    }

    public void setDescriptionShort(String descriptionShort) {
        if (StringUtils.isNotBlank(descriptionShort)) {
            this.descriptionShort = descriptionShort;
        }
    }

    public void setPhotoUrl(String photoUrl) {
        if (StringUtils.isNotBlank(photoUrl)) {
            this.photoUrl = photoUrl;
        }
    }

    public static Club.ClubBuilder builder(String name) {
        return hiddenBuilder()
                .name(name);
    }
}
