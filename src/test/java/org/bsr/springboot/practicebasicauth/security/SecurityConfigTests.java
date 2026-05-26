/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Tests security filter chain through custom security config and stubbed controller for authentication and
 *          authorization scenarios. Web Slice: Security
 * Created: 05/16/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.security;

import org.bsr.springboot.practicebasicauth.ProdTestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProdTestController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class SecurityConfigTests {

    @Autowired
    MockMvc mockMvc;


    @Test
    void shouldReturn401_whenNotAuthorized() throws Exception{
        mockMvc.perform(get("/api/me"))
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
    @WithMockUser
    void shouldReturn200_whenAuthorized() throws Exception{
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access").value("granted"))
                .andExpect(jsonPath("$.role-level").value("standard"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturn404_whenNotAdmin() throws Exception{
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON+";charset=UTF-8"))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.detail").value("Resource not found."));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn200_whenAdmin() throws Exception{
        mockMvc.perform(get("/api/admin/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access").value("granted"))
                .andExpect(jsonPath("$.role-level").value("privileged"));
    }
}
