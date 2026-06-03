/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    A simple Service class for delegating client requests to the repository and mapper
 * Created: 05/26/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.features;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    public AppUserResponse getAppUser(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        List<String> roles = appUserRepository.findRolesByUsername(username);

        return appUserMapper.toResponse(appUser, roles);
    }

    public List<AppUserResponse> getAllAppUsers() {

        return appUserRepository.findAllAppUsersAndRoles()
                .entrySet()
                .stream()
                .map(entry -> appUserMapper.toResponse(entry.getKey(), entry.getValue()))
                .toList();

    }

}
