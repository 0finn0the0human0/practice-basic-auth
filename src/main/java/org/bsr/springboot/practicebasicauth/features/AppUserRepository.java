/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Repository uses jdbctemplate and rowmapper to map rows from database to App users.
 * Created: 05/18/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.features;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Repository
public class AppUserRepository {

    private static final class AppUserRowMapper implements RowMapper<AppUser> {

        @Override
        public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            AppUser appUser = new AppUser();
            appUser.setUsername(rs.getString("USERNAME"));
            appUser.setPassword(rs.getString("HASHED_PASSWORD"));
            appUser.setStatus(rs.getString("STATUS"));
            appUser.setCreatedAt(rs.getObject("CREATED_AT", LocalDateTime.class));
            appUser.setUpdatedAt(rs.getObject("UPDATED_AT", LocalDateTime.class));
            return appUser;
        }
    }

    private static final RowMapper<AppUser> APP_USER_ROW_MAPPER = new AppUserRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AppUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate =jdbcTemplate;
    }

    public AppUser findByUsername(String username) {
        String sql = "SELECT USERNAME, HASHED_PASSWORD, STATUS, CREATED_AT, UPDATED_AT FROM USERS WHERE USERNAME = ?";

        return jdbcTemplate.queryForObject(sql, APP_USER_ROW_MAPPER, username);
    }

}
