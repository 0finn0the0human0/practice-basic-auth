/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    A simple POJO to model users entity.
 * Created: 05/18/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.features;

import java.time.LocalDateTime;
import java.util.Objects;

public class AppUser {

    private String username;
    private String password;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AppUser appUser)) return false;
        return Objects.equals(username, appUser.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
