package wszib.javazaawansowana.zadanie1.repository.jdbc;

import wszib.javazaawansowana.zadanie1.config.DatabaseConfig;
import wszib.javazaawansowana.zadanie1.model.Category;
import wszib.javazaawansowana.zadanie1.repository.CategoryRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcCategoryRepository implements CategoryRepository {
    private static final Logger LOGGER = Logger.getLogger(JdbcCategoryRepository.class.getName());

    @Override
    public List<Category> findAll() {
        List<Category> res = new ArrayList<>();
        String sql = "SELECT id, name FROM categories";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category c = new Category(rs.getLong("id"), rs.getString("name"));
                res.add(c);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
        }
        return res;
    }

    @Override
    public Category findByName(String name) {
        String sql = "SELECT id, name FROM categories WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Category(rs.getLong("id"), rs.getString("name"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding category", e);
        }
        return null;
    }

    @Override
    public void save(Category category) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving category", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting category", e);
        }
        return false;
    }
}

