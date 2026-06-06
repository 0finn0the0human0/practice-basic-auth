/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    A simple controller for returning the authenticated user's non-sensitive profile details and granted roles
 * Created: 05/27/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.features;

import org.bsr.springboot.practicebasicauth.security.AppUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<AppUserResponse> getCurrentUserDetails(@AuthenticationPrincipal AppUserPrincipal appUserPrincipal) {
        AppUserResponse appUserResponse = appUserService.getAppUser(appUserPrincipal.getUsername());
        return ResponseEntity.ok(appUserResponse);

    }

    @GetMapping("/admin/me")
    public ResponseEntity<List<AppUserResponse>> getCurrentAdminDetails() {
        List<AppUserResponse> appUserResponse = appUserService.getAllAppUsers();
        return ResponseEntity.ok(appUserResponse);

    }
}
