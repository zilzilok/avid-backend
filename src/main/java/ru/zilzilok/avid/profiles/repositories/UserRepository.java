package ru.zilzilok.avid.profiles.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zilzilok.avid.profiles.models.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByActivationCode(String code);

    List<User> findBySecondNameStartingWithIgnoreCase(String startWith, Pageable pageable);

    List<User> findByFirstNameStartingWithIgnoreCase(String startWith, Pageable pageable);

    List<User> findByFirstNameStartingWithAndSecondNameStartingWithIgnoreCase(String firstName, String secondName, Pageable pageable);
}
