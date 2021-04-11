package ru.zilzilok.avid.profiles.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zilzilok.avid.profiles.models.other.UserGame;
import ru.zilzilok.avid.profiles.models.other.UserGameId;

import java.util.List;

@Repository
public interface UserGameRepository extends CrudRepository<UserGame, UserGameId> {
    UserGame findByIdGameIdAndIdUserId(Long gameId, Long userId);

    List<UserGame> findByIdUserIdOrderByCreatingDateTimeAsc(Long userId);

    List<UserGame> findByIdUserIdOrderByCreatingDateTimeDesc(Long userId);
}
