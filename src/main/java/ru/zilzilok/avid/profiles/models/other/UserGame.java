package ru.zilzilok.avid.profiles.models.other;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.profiles.models.entities.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_games")
public class UserGame {
    @EmbeddedId
    private UserGameId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id")
    private BoardGame game;

    @Type(type="text")
    private String review;

    private double rating;

    private Timestamp creatingDateTime;

    public UserGame(User owner, BoardGame game, String review, double rating) {
        this.owner = owner;
        this.game = game;
        this.review = review;
        this.rating = rating;
        // Moscow time - 3 hours = UTC
//        this.creatingDateTime = new Timestamp(new DateTime(DateTimeZone.UTC).minusHours(3).getMillis());
        this.creatingDateTime = new Timestamp(new DateTime(DateTimeZone.UTC).getMillis());
        this.id = new UserGameId(owner.getId(), game.getId());
    }
}
