package wszib.javazaawansowana.zadanie1.repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import wszib.javazaawansowana.zadanie1.config.DatabaseConfig;
import wszib.javazaawansowana.zadanie1.model.Loan;
import wszib.javazaawansowana.zadanie1.repository.LoanRepository;

public class JdbcLoanRepository implements LoanRepository {
    private static final Logger LOGGER = Logger.getLogger(JdbcLoanRepository.class.getName());

    @Override
    public boolean createLoan(Loan loan) {
        String sql = "INSERT INTO loans (user_id, book_id, loan_date) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, loan.getUserId());
            ps.setLong(2, loan.getBookId());
            ps.setTimestamp(3, Timestamp.valueOf(loan.getLoanDate()));
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (PreparedStatement ups = conn.prepareStatement("UPDATE books SET available = FALSE WHERE id = ?")) {
                    ups.setLong(1, loan.getBookId());
                    ups.executeUpdate();
                }
                return true;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating loan", e);
        }
        return false;
    }

    @Override
    public boolean returnLoan(Long loanId) {
        String sql = "UPDATE loans SET return_date = ? WHERE id = ? AND return_date IS NULL";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(2, loanId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (PreparedStatement get = conn.prepareStatement("SELECT book_id FROM loans WHERE id = ?")) {
                    get.setLong(1, loanId);
                    try (ResultSet rs = get.executeQuery()) {
                        if (rs.next()) {
                            long bookId = rs.getLong("book_id");
                            try (PreparedStatement ups = conn.prepareStatement("UPDATE books SET available = TRUE WHERE id = ?")) {
                                ups.setLong(1, bookId);
                                ups.executeUpdate();
                            }
                        }
                    }
                }
                return true;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error returning loan", e);
        }
        return false;
    }

    @Override
    public List<Loan> findActiveLoansByUserId(Long userId) {
        List<Loan> res = new ArrayList<>();
        String sql = "SELECT id, user_id, book_id, loan_date, return_date FROM loans WHERE user_id = ? AND return_date IS NULL";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Loan l = new Loan();
                    l.setId(rs.getLong("id"));
                    l.setUserId(rs.getLong("user_id"));
                    l.setBookId(rs.getLong("book_id"));
                    l.setLoanDate(rs.getTimestamp("loan_date").toLocalDateTime());
                    res.add(l);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding active loans", e);
        }
        return res;
    }
}

