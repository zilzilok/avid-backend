package ru.zilzilok.avid.сlubs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.zilzilok.avid.profiles.models.entities.User;
import ru.zilzilok.avid.tools.OffsetBasedPageRequest;
import ru.zilzilok.avid.сlubs.models.dto.ClubDto;
import ru.zilzilok.avid.сlubs.models.entities.Club;
import ru.zilzilok.avid.сlubs.repositories.ClubRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ClubService {
    private final ClubRepository clubRepo;

    @Autowired
    public ClubService(ClubRepository clubRepo) {
        this.clubRepo = clubRepo;
    }

    @Transactional
    public Club save(Club club) {
        return clubRepo.save(club);
    }

    @Transactional
    public Club findById(Long id) {
        Optional<Club> club = clubRepo.findById(id);
        return club.orElse(null);
    }

    @Transactional
    public Club findByName(String name) {
        return clubRepo.findByName(name);
    }

    @Transactional
    public Iterable<Club> getAll(int limit, int offset, Sort sort) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit, sort);
        return clubRepo.findAll(pageable).getContent();
    }

    public Club createNewClub(ClubDto clubDto, User creator) {
        Club club = Club.builder(clubDto.getName())
                .description(clubDto.getDescription())
                .descriptionShort(clubDto.getDescriptionShort())
                .photoUrl(clubDto.getPhotoUrl())
                .creator(creator)
                .member(creator)
                .build();
        return clubRepo.save(club);
    }
}
