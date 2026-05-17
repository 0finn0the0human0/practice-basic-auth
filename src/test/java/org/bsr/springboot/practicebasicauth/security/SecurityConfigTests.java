/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Tests security filter chain through custom security config and stubbed controller for authentication and
 *          authorization scenarios. Web Slice: Security
 * Created: 05/16/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.security;

import org.bsr.springboot.practicebasicauth.testUtil.TestSecuredController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestSecuredController.class)
@Import(SecurityConfig.class)
public class SecurityConfigTests {

    @Autowired
    MockMvc mockMvc;


    @Test
    void shouldReturn401_whenNotAuthorized() throws Exception{
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldReturn200_whenAuthorized() throws Exception{
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturn403_whenNotAdmin() throws Exception{
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn200_whenAdmin() throws Exception{
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isOk()).andExpect(content().string("Secret: SUCCESS"));
    }
}
