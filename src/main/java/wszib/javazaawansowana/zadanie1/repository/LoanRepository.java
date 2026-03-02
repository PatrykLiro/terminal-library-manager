package wszib.javazaawansowana.zadanie1.repository;

import wszib.javazaawansowana.zadanie1.model.Loan;

import java.util.List;

public interface LoanRepository {
    boolean createLoan(Loan loan);
    boolean returnLoan(Long loanId);
    List<Loan> findActiveLoansByUserId(Long userId);
}

