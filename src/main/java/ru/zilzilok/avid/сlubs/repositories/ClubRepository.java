package ru.zilzilok.avid.сlubs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zilzilok.avid.сlubs.models.entities.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByName(String name);
}
