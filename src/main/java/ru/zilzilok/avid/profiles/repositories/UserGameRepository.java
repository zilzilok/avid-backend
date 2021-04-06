package ru.zilzilok.avid.profiles.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zilzilok.avid.profiles.models.other.UserGame;
import ru.zilzilok.avid.profiles.models.other.UserGameId;

@Repository
public interface UserGameRepository extends CrudRepository<UserGame, UserGameId> {
    UserGame findByIdGameIdAndIdUserId(Long gameId, Long userId);
}
