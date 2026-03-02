package wszib.javazaawansowana.zadanie1;

import wszib.javazaawansowana.zadanie1.LoginPanel.LoginPanel;
import wszib.javazaawansowana.zadanie1.adapters.BooksAdapter;
import wszib.javazaawansowana.zadanie1.config.DatabaseInitializer;
import wszib.javazaawansowana.zadanie1.gui.Gui;
import wszib.javazaawansowana.zadanie1.repository.jdbc.JdbcBookRepository;
import wszib.javazaawansowana.zadanie1.repository.jdbc.JdbcCategoryRepository;
import wszib.javazaawansowana.zadanie1.repository.jdbc.JdbcLoanRepository;
import wszib.javazaawansowana.zadanie1.repository.jdbc.JdbcUserRepository;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.init();

        JdbcUserRepository userRepo = new JdbcUserRepository();
        JdbcBookRepository bookRepo = new JdbcBookRepository();
        JdbcLoanRepository loanRepo = new JdbcLoanRepository();
        JdbcCategoryRepository categoryRepo = new JdbcCategoryRepository();
        LoginPanel loginPanel = new LoginPanel(userRepo);
        Gui gui = new Gui(new BooksAdapter(bookRepo), loginPanel, loanRepo, categoryRepo);
        gui.start();
    }
}
