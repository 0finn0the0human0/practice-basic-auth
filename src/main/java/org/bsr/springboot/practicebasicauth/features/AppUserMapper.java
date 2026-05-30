/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    A simple mapper component to map repository calls to data transfer objects
 * Created: 05/26/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.features;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppUserMapper {

    public AppUserResponse toResponse(AppUser appUser, List<String> roles) {
        return new AppUserResponse(appUser.getUsername(), appUser.getStatus(), roles);
    }
}
