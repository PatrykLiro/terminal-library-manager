package wszib.javazaawansowana.zadanie1.repository.jdbc;

import wszib.javazaawansowana.zadanie1.config.DatabaseConfig;
import wszib.javazaawansowana.zadanie1.model.Account;
import wszib.javazaawansowana.zadanie1.repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcUserRepository implements UserRepository {
    private static final Logger LOGGER = Logger.getLogger(JdbcUserRepository.class.getName());

    @Override
    public Account findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, role FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account a = new Account();
                    a.setId(rs.getLong("id"));
                    a.setUsername(rs.getString("username"));
                    a.setPasswordHash(rs.getString("password_hash"));
                    a.setRole(rs.getString("role"));
                    return a;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding user by username: " + username, e);
        }
        return null;
    }

    @Override
    public void save(Account account) {
        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPasswordHash());
            ps.setString(3, account.getRole());
            ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving user: " + account.getUsername(), e);
        }
    }
}
