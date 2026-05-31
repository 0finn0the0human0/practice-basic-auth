/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Slice Test class for AppUserRepository methods. Ensures proper handling of objects within database calls.
 * Created: 05/18/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.features;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(AppUserRepository.class)
class AppUserRepoTests {

    // "admin1" is a valid username stored in test db with 2 roles [ADMIN, USER]
    String validUsername = "admin1";

    @Autowired
    AppUserRepository appUserRepository;

    @Test
    void shouldReturnUser_whenUsernameFound() {
        AppUser appUser = appUserRepository.findByUsername(validUsername);

        assertThat(appUser).isNotNull();
        assertThat(appUser.getStatus()).isEqualTo("ACTIVE");
        assertThat(appUser.getCreatedAt()).isInstanceOf(LocalDateTime.class).isNotNull();
    }

    @Test
    void shouldReturnException_whenUsernameNotFound() {
        assertThatThrownBy(() -> appUserRepository.findByUsername("invalid-username"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldReturnRoles_whenUsernameFound() {
        List<String> roles = appUserRepository.findRolesByUsername(validUsername);

        assertThat(roles.size()).isEqualTo(2);
        assertThat(roles.getFirst()).isEqualTo("ADMIN");
        assertThat(roles.getLast()).isEqualTo("USER");
    }

    @Test
    void shouldReturnAllAppUsers() {
        List<AppUser> appUsers = appUserRepository.findAll();

        assertThat(appUsers.size()).isEqualTo(2);
        assertThat(appUsers.getFirst().getUsername()).isEqualTo("admin1");
        assertThat(appUsers.getLast().getUsername()).isEqualTo("user1");
    }
}
