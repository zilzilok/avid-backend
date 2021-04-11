package ru.zilzilok.avid.profiles.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zilzilok.avid.profiles.models.other.UserGame;
import ru.zilzilok.avid.profiles.models.other.UserGameId;

import java.util.List;

@Repository
public interface UserGameRepository extends CrudRepository<UserGame, UserGameId> {
    UserGame findByIdGameIdAndIdUserId(Long gameId, Long userId);

    List<UserGame> findByIdUserId(Long userId);

    List<UserGame> findByIdUserIdOrderByCreatingDateTimeAsc(Long userId);

    List<UserGame> findByIdUserIdOrderByCreatingDateTimeDesc(Long userId);

    @Query("SELECT fg FROM User u JOIN u.friends f JOIN f.games fg WHERE u.id = :userId")
    List<UserGame> findFriendsGames(Long userId, Pageable pageable);

    @Query("SELECT fg FROM User u JOIN u.friends f JOIN f.games fg WHERE u.id = :userId ORDER BY fg.creatingDateTime ASC")
    List<UserGame> findFriendsGamesOrderByCreatingDateTimeAsc(Long userId, Pageable pageable);

    @Query("SELECT fg FROM User u JOIN u.friends f JOIN f.games fg WHERE u.id = :userId ORDER BY fg.creatingDateTime DESC")
    List<UserGame> findFriendsGamesOrderByCreatingDateTimeDesc(Long userId, Pageable pageable);
}
