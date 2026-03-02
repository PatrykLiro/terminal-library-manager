package wszib.javazaawansowana.zadanie1.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import wszib.javazaawansowana.zadanie1.util.PasswordHash;

public class DatabaseInitializer {
    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());
    public static void init() {
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {
            InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream("schema.sql");
            if (is == null) {
                System.out.println("schema.sql not found in resources");
                return;
            }
            String sql = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
            for (String s : sql.split(";")) {
                String trim = s.trim();
                if (!trim.isEmpty()) {
                    try {
                        stmt.execute(trim);
                    } catch (SQLException e) {
                        String msg = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
                        String state = e.getSQLState();
                        int code = e.getErrorCode();
                        if ((state != null && state.equals("23505")) || code == 23505 || msg.contains("unique") || msg.contains("duplicate")) {
                            LOGGER.log(Level.INFO, "Ignoruję naruszenie ograniczenia unikalności podczas wykonywania: " + trim + " - " + e.getMessage());
                        } else {
                            LOGGER.log(Level.WARNING, "Błąd podczas wykonywania instrukcji SQL: " + trim, e);
                        }
                    }
                }
            }
            System.out.println("Database initialized.");

            String selectSql = "SELECT id, password_hash FROM users";
            try (PreparedStatement ps = conn.prepareStatement(selectSql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String pwd = rs.getString("password_hash");
                    if (pwd != null && !pwd.startsWith("$2")) {
                        String hashed = PasswordHash.hash(pwd);
                        try (PreparedStatement ups = conn.prepareStatement("UPDATE users SET password_hash = ? WHERE id = ?")) {
                            ups.setString(1, hashed);
                            ups.setLong(2, id);
                            ups.executeUpdate();
                        }
                    }
                }
            }
            System.out.println("User passwords hashed.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing database", e);
        }
    }
}
