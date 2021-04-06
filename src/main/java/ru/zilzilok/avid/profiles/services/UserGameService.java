package ru.zilzilok.avid.profiles.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.models.other.UserGame;
import ru.zilzilok.avid.profiles.repositories.UserGameRepository;

import javax.transaction.Transactional;

@Service
@EnableTransactionManagement
public class UserGameService {
    private final UserGameRepository userGameRepo;

    @Autowired
    public UserGameService(UserGameRepository userGameRepo) {
        this.userGameRepo = userGameRepo;
    }

    @Transactional
    public UserGame findById(Long userId, Long gameId) {
        return userGameRepo.findByIdGameIdAndIdUserId(gameId, userId);
    }


    @Transactional
    public void addGame(User user, BoardGame boardGame, String review, double rating) {
        UserGame userGame = new UserGame(user, boardGame, review, rating);

        user.getGames().add(userGame);
        boardGame.getOwners().add(userGame);
    }

    @Transactional
    public void removeGame(User user, BoardGame boardGame) {
        UserGame userGame = new UserGame(user, boardGame);

        user.getGames().remove(userGame);
        boardGame.getOwners().remove(userGame);
    }
}