package wszib.javazaawansowana.zadanie1.repository.jdbc;

import wszib.javazaawansowana.zadanie1.config.DatabaseConfig;
import wszib.javazaawansowana.zadanie1.model.Book;
import wszib.javazaawansowana.zadanie1.repository.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcBookRepository implements BookRepository {
    private static final Logger LOGGER = Logger.getLogger(JdbcBookRepository.class.getName());

    @Override
    public List<Book> findAll() {
        List<Book> res = new ArrayList<>();
        String sql = "SELECT id, isbn, title, author, available FROM books";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Book b = new Book(rs.getLong("id"), rs.getString("isbn"), rs.getString("title"), rs.getString("author"), rs.getBoolean("available"));
                res.add(b);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all books", e);
        }
        return res;
    }

    @Override
    public List<Book> searchByTitleOrAuthor(String q) {
        List<Book> res = new ArrayList<>();
        String sql = "SELECT id, isbn, title, author, available FROM books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String p = "%" + q.toLowerCase() + "%";
            ps.setString(1, p);
            ps.setString(2, p);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book b = new Book(rs.getLong("id"), rs.getString("isbn"), rs.getString("title"), rs.getString("author"), rs.getBoolean("available"));
                    res.add(b);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching books", e);
        }
        return res;
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (isbn, title, author, available) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setBoolean(4, book.isAvailable());
            ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving book: " + book.getIsbn(), e);
        }
    }

    @Override
    public boolean deleteByIsbn(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting book: " + isbn, e);
        }
        return false;
    }

    @Override
    public Book findByIsbn(String isbn) {
        String sql = "SELECT id, isbn, title, author, available FROM books WHERE isbn = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Book(rs.getLong("id"), rs.getString("isbn"), rs.getString("title"), rs.getString("author"), rs.getBoolean("available"));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding book by isbn: " + isbn, e);
        }
        return null;
    }

    @Override
    public boolean update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, available = ? WHERE isbn = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setBoolean(3, book.isAvailable());
            ps.setString(4, book.getIsbn());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating book: " + book.getIsbn(), e);
        }
        return false;
    }
}
