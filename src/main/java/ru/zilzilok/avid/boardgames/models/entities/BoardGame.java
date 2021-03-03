package ru.zilzilok.avid.boardgames.models.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

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
    private List<String> titles;

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
