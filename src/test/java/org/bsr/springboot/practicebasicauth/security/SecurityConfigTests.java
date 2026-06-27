/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Tests security filter chain through custom security config and stubbed controller for authentication and
 *          authorization scenarios. Web Slice: Security
 * Created: 05/16/2026
 * Version: 1.0
 * Updated: 6/5/26
 * Changes: Moving complex mocking into integration SpringBootTest class and replacing with access only checks with
 *          status
 * */

package org.bsr.springboot.practicebasicauth.security;

import org.bsr.springboot.practicebasicauth.features.AppUser;
import org.bsr.springboot.practicebasicauth.features.AppUserController;
import org.bsr.springboot.practicebasicauth.features.AppUserResponse;
import org.bsr.springboot.practicebasicauth.features.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@WebMvcTest(controllers = AppUserController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class,
        SecurityAuditLoggerUtil.class, AuthenticationEventLogger.class})
class SecurityConfigTests {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    AppUserService appUserService;


    @Test
    void shouldReturn401_whenNotAuthorized() throws Exception{
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn200_whenAuthorized() throws Exception{
        AppUser appUser =  new AppUser();
        appUser.setUsername("user1");
        appUser.setPassword("secret");
        appUser.setStatus("ACTIVE");
        AppUserResponse appUserResponse = new AppUserResponse(appUser.getUsername(), appUser.getStatus(), List.of("USER"));
        List<GrantedAuthority> authorities = Stream.of("USER")
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_"+role))
                .toList();
        AppUserPrincipal principal = new AppUserPrincipal(appUser, authorities);
        when(appUserService.getAppUser(appUserResponse.username())).thenReturn(appUserResponse);

        mockMvc.perform(get("/api/me").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturn404_whenNotAdmin() throws Exception{
        mockMvc.perform(get("/api/admin/me"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON+";charset=UTF-8"))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.detail").value("Resource not found."));
    }


    @Test
    @WithMockUser( roles = "ADMIN")
    void shouldReturnOk_whenAdmin() throws Exception{
        mockMvc.perform(get("/api/admin/me")).andExpect(status().isOk());
    }
}
