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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(AppUserRepository.class)
class AppUserRepoTests {

    @Autowired
    AppUserRepository appUserRepository;

    @Test
    void shouldReturnUser_whenUsernameFound() {
        AppUser appUser = appUserRepository.findByUsername("user1");

        assertThat(appUser).isNotNull();
        assertThat(appUser.getStatus()).isEqualTo("ACTIVE");
        assertThat(appUser.getCreatedAt()).isInstanceOf(LocalDateTime.class).isNotNull();
    }

    @Test
    void shouldReturnException_whenUsernameNotFound() {
        assertThatThrownBy(() -> appUserRepository.findByUsername("invalid-username"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
