package ru.zilzilok.avid.boardgames.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.zilzilok.avid.boardgames.models.entities.BoardGame;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<BoardGame, Long> {
    BoardGame findByAlias(String alias);

    @Query("SELECT DISTINCT bg FROM BoardGame bg JOIN bg.titles t WHERE LOWER(t) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<BoardGame> findByTitlesContainingIgnoreCase(String title, Pageable pageable);
}
