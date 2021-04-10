package ru.zilzilok.avid.boardgames.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Parameter;

import ru.zilzilok.avid.profiles.models.other.UserGame;

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

    @ElementCollection
    @Singular
    @CollectionTable(name = "board_game_titles", joinColumns = @JoinColumn(name = "board_game_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<String> titles;

    @OneToMany(
            mappedBy = "game",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonIgnore
    @Singular
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UserGame> owners;

    @Column(unique = true)
    private String alias;
    @Type(type = "text")
    private String description;
    @Type(type = "text")
    private String descriptionShort;
    private String photoUrl;
    private int year;
    private int playersMin;
    private int playersMax;
    private int playersMinRecommend;
    private int playersMaxRecommend;
    private int playtimeMin;
    private int playtimeMax;
    private int playersAgeMin;
    @Transient
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private double averageRating;
}
