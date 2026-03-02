package wszib.javazaawansowana.zadanie1.gui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import wszib.javazaawansowana.zadanie1.LoginPanel.ILoginPanel;
import wszib.javazaawansowana.zadanie1.database.IBooks;
import wszib.javazaawansowana.zadanie1.model.Account;
import wszib.javazaawansowana.zadanie1.model.Book;
import wszib.javazaawansowana.zadanie1.model.Category;
import wszib.javazaawansowana.zadanie1.model.Loan;
import wszib.javazaawansowana.zadanie1.repository.CategoryRepository;
import wszib.javazaawansowana.zadanie1.repository.LoanRepository;

public class Gui implements IGui {
    private final IBooks books;
    private final ILoginPanel loginPanel;
    private final LoanRepository loanRepository;
    private final CategoryRepository categoryRepository;
    private final Scanner scanner;
    private Account currentAccount;

    public Gui(IBooks books, ILoginPanel loginPanel, LoanRepository loanRepository, CategoryRepository categoryRepository) {
        this.books = books;
        this.loginPanel = loginPanel;
        this.loanRepository = loanRepository;
        this.categoryRepository = categoryRepository;
        this.scanner = new Scanner(System.in);
    }


    @Override
    public void start() {
        System.out.println("Witaj w bibliotece!");
        while (true) {
            System.out.print("Login: ");
            String user = scanner.nextLine().trim();
            System.out.print("Hasło: ");
            String pass = scanner.nextLine().trim();
            Account account = loginPanel.authenticate(user, pass);
            if (account != null) {
                currentAccount = account;
                System.out.println("Zalogowano jako: " + account.getUsername() + " (" + account.getRole() + ")");
                if ("ADMIN".equalsIgnoreCase(account.getRole())) {
                    adminLoop();
                } else {
                    userLoop();
                }
                currentAccount = null;
            } else {
                System.out.println("Błędne dane. Spróbuj ponownie.");
            }
        }
    }

    public void userLoop() {
        while (true) {
            System.out.println("--- MENU USER ---");
            System.out.println("1. Lista książek");
            System.out.println("2. Szukaj");
            System.out.println("3. Wypożycz książkę");
            System.out.println("4. Moje wypożyczenia");
            System.out.println("5. Zwróć książkę");
            System.out.println("0. Wyloguj");
            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    listBooks();
                    break;
                case "2":
                    searchBooks();
                    break;
                case "3":
                    borrowBook();
                    break;
                case "4":
                    myLoans();
                    break;
                case "5":
                    returnBook();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Nieznana opcja");
            }
        }
    }

    public void adminLoop() {
        while (true) {
            System.out.println("\n--- MENU ADMIN ---");
            System.out.println("1. Lista książek");
            System.out.println("2. Szukaj");
            System.out.println("3. Dodaj książkę");
            System.out.println("4. Usuń książkę");
            System.out.println("5. Edytuj książkę");
            System.out.println("6. Kategorie");
            System.out.println("7. Statystyki");
            System.out.println("0. Wyloguj");
            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    listBooks();
                    break;
                case "2":
                    searchBooks();
                    break;
                case "3":
                    addBook();
                    break;
                case "4":
                    removeBook();
                    break;
                case "5":
                    editBook();
                    break;
                case "6":
                    manageCategories();
                    break;
                case "7":
                    showStats();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Nieznana opcja");
            }
        }
    }

    public void listBooks() {
        List<Book> all = books.findAll();
        System.out.println("--- Lista książek ---");
        for (Book b : all) {
            System.out.println("ISBN: " + b.getIsbn() + ", Tytuł: " + b.getTitle() + ", Autor: " + b.getAuthor());
        }
    }

    public void searchBooks() {
        System.out.print("Szukaj: ");
        String q = scanner.nextLine().trim();
        List<Book> found = books.searchByTitleOrAuthor(q);
        if (found.isEmpty()) {
            System.out.println("Brak wyników.");
        } else {
            for (Book b : found) {
                System.out.println("ISBN: " + b.getIsbn() + ", Tytuł: " + b.getTitle() + ", Autor: " + b.getAuthor());
            }
        }
    }

    public void addBook() {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Tytuł: ");
        String title = scanner.nextLine().trim();
        System.out.print("Autor: ");
        String author = scanner.nextLine().trim();
        books.add(new Book(null, isbn, title, author, true));
        System.out.println("Dodano książkę.");
    }

    public void removeBook() {
        System.out.print("Podaj ISBN do usunięcia: ");
        String isbn = scanner.nextLine().trim();
        boolean ok = books.removeByIsbn(isbn);
        System.out.println(ok ? "Usunięto." : "Nie znaleziono książki o takim ISBN.");
    }

    public void editBook() {
        System.out.print("Podaj ISBN do edycji: ");
        String isbn = scanner.nextLine().trim();
        Book b = books.findByIsbn(isbn);
        if (b == null) {
            System.out.println("Nie znaleziono książki.");
            return;
        }
        System.out.print("Nowy tytuł: ");
        String title = scanner.nextLine().trim();
        System.out.print("Nowy autor: ");
        String author = scanner.nextLine().trim();
        if (!title.isEmpty()) b.setTitle(title);
        if (!author.isEmpty()) b.setAuthor(author);
        boolean ok = books.update(b);
        System.out.println(ok ? "Zaktualizowano." : "Aktualizacja nie powiodła się.");
    }

    private void borrowBook() {
        System.out.print("Podaj ISBN książki do wypożyczenia: ");
        String isbn = scanner.nextLine().trim();
        Book b = books.findByIsbn(isbn);
        if (b == null) {
            System.out.println("Nie znaleziono książki.");
            return;
        }
        if (!b.isAvailable()) {
            System.out.println("Książka niedostępna.");
            return;
        }
        if (currentAccount == null) { System.out.println("Brak zalogowanego użytkownika."); return; }
        Loan loan = new Loan();
        loan.setUserId(currentAccount.getId());
        loan.setBookId(b.getId());
        loan.setLoanDate(LocalDateTime.now());
        boolean ok = loanRepository.createLoan(loan);
        System.out.println(ok ? "Wypożyczono książkę." : "Nie udało się wypożyczyć książki.");
    }

    private void myLoans() {
        if (currentAccount == null) { System.out.println("Brak zalogowanego użytkownika."); return; }
        List<Loan> loans = loanRepository.findActiveLoansByUserId(currentAccount.getId());
        if (loans.isEmpty()) {
            System.out.println("Brak aktywnych wypożyczeń.");
        } else {
            for (Loan l : loans) {
                System.out.println("Loan ID: " + l.getId() + ", Book ID: " + l.getBookId() + ", Date: " + l.getLoanDate());
            }
        }
    }

    private void returnBook() {
        System.out.print("Podaj ID wypożyczenia do zwrotu: ");
        String sid = scanner.nextLine().trim();
        long id;
        try { id = Long.parseLong(sid); } catch (Exception e) { System.out.println("Błędne ID."); return; }
        boolean ok = loanRepository.returnLoan(id);
        System.out.println(ok ? "Zwrócono książkę." : "Nie znaleziono wypożyczenia.");
    }

    private void manageCategories() {
        while (true) {
            System.out.println("--- Kategorie ---");
            System.out.println("1. Lista kategorii");
            System.out.println("2. Dodaj kategorię");
            System.out.println("3. Usuń kategorię");
            System.out.println("0. Powrót");
            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    List<Category> cats = categoryRepository.findAll();
                    for (Category c : cats) System.out.println(c.getId() + ": " + c.getName());
                    break;
                case "2":
                    System.out.print("Nazwa kategorii: ");
                    String name = scanner.nextLine().trim();
                    categoryRepository.save(new Category(null, name));
                    System.out.println("Dodano kategorię.");
                    break;
                case "3":
                    System.out.print("Podaj ID kategorii do usunięcia: ");
                    String sid = scanner.nextLine().trim();
                    try { Long id = Long.parseLong(sid); if (categoryRepository.deleteById(id)) System.out.println("Usunięto."); else System.out.println("Nie znaleziono."); } catch (Exception e) { System.out.println("Błędne ID."); }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Nieznana opcja");
            }
        }
    }

    private void showStats() {
        List<Book> all = books.findAll();
        long total = all.size();
        long borrowed = all.stream().filter(b -> !b.isAvailable()).count();
        System.out.println("Liczba wszystkich książek: " + total);
        System.out.println("Liczba wypożyczonych książek: " + borrowed);
    }
}
