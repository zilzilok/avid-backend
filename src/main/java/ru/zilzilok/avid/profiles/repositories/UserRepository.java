package ru.zilzilok.avid.profiles.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.zilzilok.avid.profiles.models.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByActivationCode(String code);

    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT(:startsWith, '%')) " +
            "OR LOWER(u.secondName) LIKE LOWER(CONCAT(:startsWith, '%')) " +
            "OR LOWER(u.username) LIKE LOWER(CONCAT(:startsWith, '%'))")
    List<User> findByNameStartingWithIgnoreCase(String startsWith, Pageable pageable);

    @Query("SELECT DISTINCT f FROM User u " +
            "JOIN u.friends f " +
            "WHERE " +
            "(u.username = :username " +
            "AND (LOWER(f.firstName) LIKE LOWER(CONCAT(:startsWith, '%')) " +
            "OR LOWER(f.secondName) LIKE LOWER(CONCAT(:startsWith, '%')) " +
            "OR LOWER(f.username) LIKE LOWER(CONCAT(:startsWith, '%')))" +
            ") " +
            "ORDER BY f.secondName")
    List<User> findFriendsByNameStartingWithIgnoreCase(String username, String startsWith);
}
