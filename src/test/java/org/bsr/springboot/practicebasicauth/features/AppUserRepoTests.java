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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

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

    // TODO: Finish implementing tests for repo method after custom exception for usernotfound
}
