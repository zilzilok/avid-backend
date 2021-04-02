package ru.zilzilok.avid.boardgames.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;
import ru.zilzilok.avid.profiles.models.entities.User;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "board_games")
public class BoardGame {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ElementCollection
    @Singular
    @CollectionTable(name = "board_game_titles", joinColumns = @JoinColumn(name = "board_game_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<String> titles;

    @ManyToMany(
            targetEntity = User.class, mappedBy = "games",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )
    @JsonIgnore
    @Singular
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> owners;

    @Column(unique = true)
    private String alias;
    @Type(type="text")
    private String description;
    @Type(type="text")
    private String descriptionShort;
    private String photoUrl;
    private int year;
    private int playersMin;
    private int playersMax;
    private int playersMinRecommend;
    private int playersMaxRecommend;
}
