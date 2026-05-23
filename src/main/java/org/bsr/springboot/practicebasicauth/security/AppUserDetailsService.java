/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Service class loads AppUser data for Spring Security during authentication.
 * Created: 05/19/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.security;

import org.bsr.springboot.practicebasicauth.features.AppUser;
import org.bsr.springboot.practicebasicauth.features.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository =appUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        List<GrantedAuthority> authorities = appUserRepository.findRolesByUsername(username)
                .stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        return new AppUserPrincipal(appUser, authorities);
    }
}
