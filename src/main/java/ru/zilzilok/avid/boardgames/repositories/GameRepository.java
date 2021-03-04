package ru.zilzilok.avid.boardgames.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;

@Repository
public interface GameRepository extends JpaRepository<BoardGame, Long> {
    BoardGame findByAlias(String alias);
}
