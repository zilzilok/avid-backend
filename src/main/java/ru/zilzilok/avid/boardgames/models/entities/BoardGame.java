package ru.zilzilok.avid.boardgames.models.entities;

import lombok.*;

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
    private String description;
    private String descriptionShort;
    private String publisherName;
    private String photoPath;
    private int year;
    private int playersMin;
    private int playersMax;
    private int playersMinRecommend;
    private int playersMaxRecommend;
}
