/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    This class models minimal implementation of an authenticated AppUser and stored roles(authorities) to
 *          reference
 * Created: 05/19/2026
 * Version: 1.0
 * */


package org.bsr.springboot.practicebasicauth.security;

import org.bsr.springboot.practicebasicauth.features.AppUser;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AppUserPrincipal implements UserDetails {

    private final transient AppUser appUser;
    private final List<GrantedAuthority> authorities;

    public AppUserPrincipal(AppUser appUser, List<GrantedAuthority> authorities) {
        this.appUser = appUser;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getUsername();
    }
}
