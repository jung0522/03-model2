package io.goorm.backend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import io.goorm.backend.config.DatabaseConfig;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 사용자 데이터 접근 객체
 */
public class UserDAO {
    private JdbcTemplate jdbcTemplate;

    public UserDAO() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConfig.getDataSource());
    }

    // RowMapper 정의
    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setRegDate(rs.getTimestamp("reg_date"));
        return user;
    };

    /**
     * MD5 해시 함수 (UTF-8 사용)
     */
    private String hashPassword(String password) {
        try {
            password = password.trim();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return password;
        }
    }

    /**
     * 사용자 등록 (비밀번호 해시 적용)
     */
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (username, password, name, email) VALUES (?, ?, ?, ?)";
        try {
            String hashedPassword = hashPassword(user.getPassword());

            int result = jdbcTemplate.update(sql,
                    user.getUsername(),
                    hashedPassword,
                    user.getName(),
                    user.getEmail());
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 사용자명으로 사용자 조회
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, username.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 이메일로 사용자 조회
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, email.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 사용자 ID로 사용자 조회
     */
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }
}
