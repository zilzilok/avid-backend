package ru.zilzilok.avid.profile.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zilzilok.avid.profile.models.entities.Role;
import ru.zilzilok.avid.profile.repositories.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepo;

    @Autowired
    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role getOrCreate(String name) {
        Role res = roleRepo.findByName(name);
        if(res == null) {
            res = roleRepo.save(new Role(name));
        }
        return res;
    }
}
