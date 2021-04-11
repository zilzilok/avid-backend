package ru.zilzilok.avid.profiles.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ObjectUtils;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.profiles.models.other.UserGame;
import ru.zilzilok.avid.profiles.repositories.UserGameRepository;
import ru.zilzilok.avid.tools.OffsetBasedPageRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<UserGame> findByIdOrderByAsc(Long userId) {
        return userGameRepo.findByIdUserIdOrderByCreatingDateTimeAsc(userId);
    }

    @Transactional
    public List<UserGame> findByIdOrderedBy(Long userId, String sort) {
        if (!ObjectUtils.isEmpty(sort)) {
            if (sort.equalsIgnoreCase("asc".trim())) {
                return userGameRepo.findByIdUserIdOrderByCreatingDateTimeAsc(userId);
            } else if (sort.equalsIgnoreCase("desc".trim())) {
                return userGameRepo.findByIdUserIdOrderByCreatingDateTimeDesc(userId);
            }
        }
        return userGameRepo.findByIdUserId(userId);
    }

    @Transactional
    public List<UserGame> findFriendsGames(Long userId, int limit, int offset, String sort) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit);
        if (!ObjectUtils.isEmpty(sort)) {
            if (sort.equalsIgnoreCase("asc".trim())) {
                return userGameRepo.findFriendsGamesOrderByCreatingDateTimeAsc(userId, pageable);
            } else if (sort.equalsIgnoreCase("desc".trim())) {
                return userGameRepo.findFriendsGamesOrderByCreatingDateTimeDesc(userId, pageable);
            }
        }
        return userGameRepo.findFriendsGames(userId, pageable);
    }

    @Transactional
    public void addGame(User user, BoardGame boardGame, String review, double rating) {
        UserGame userGame = new UserGame(user, boardGame, review, rating);

        user.getGames().add(userGame);
        boardGame.getOwners().add(userGame);
    }

    @Transactional
    public void removeGame(User user, BoardGame boardGame, UserGame userGame) {
        user.getGames().remove(userGame);
        boardGame.getOwners().remove(userGame);
    }

    public Iterable<UserGame> addAverageRating(Iterable<UserGame> userGames){
        userGames.forEach(bg ->{
            List<Double> ratings = bg.getGame().getOwners().stream().map(UserGame::getRating).collect(Collectors.toList());
            double averageRating = ratings.size() > 0 ? ratings.stream().reduce(0., Double::sum) / ratings.size() : 0;
            bg.getGame().setAverageRating(averageRating);
        });
        return userGames;
    }
}
