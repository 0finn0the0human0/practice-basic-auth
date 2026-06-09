/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Integration tests for testing Acceptance Criteria as outlined in the readme.
 * Created: 06/5/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PracticeBasicAuthApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldReturnUnauthorizedProblemDetail_whenCredentialsAreInvalid() throws Exception{
        mockMvc.perform(get("/api/me").with(httpBasic("bad-username","bad-password")))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON+";charset=UTF-8"))
                .andExpect(header().exists("WWW-Authenticate"))
                .andExpect(jsonPath("$.instance").value("/api/me"))
                .andExpect(jsonPath("$.status").value("401"))
                .andExpect(jsonPath("$.title").value("Unauthorized"))
                .andExpect(jsonPath("$.detail").value(
                        "Authentication credentials are missing or invalid. Include a valid Authorization header."));
    }

    @Test
    void shouldReturnOkResponse_whenValidAdmin() throws Exception{

        mockMvc.perform(get("/api/admin/me").with(httpBasic("admin1", "supersecret")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin1"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[0].roles", containsInAnyOrder("ADMIN", "USER")));
    }

}
