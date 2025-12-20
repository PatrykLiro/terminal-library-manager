package wszib.javazaawansowana.zadanie1.gui;

import lombok.AllArgsConstructor;
import wszib.javazaawansowana.zadanie1.database.IBooks;
import wszib.javazaawansowana.zadanie1.LoginPanel.ILoginPanel;
import wszib.javazaawansowana.zadanie1.model.Account;
import wszib.javazaawansowana.zadanie1.model.Book;

import java.util.List;
import java.util.Scanner;

@AllArgsConstructor
public class Gui implements IGui {
    private final IBooks books;
    private final ILoginPanel loginPanel;
    private final Scanner scanner = new Scanner(System.in);


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
                System.out.println("Zalogowano jako: " + account.getUsername() + " (" + account.getRole() + ")");
                if ("ADMIN".equalsIgnoreCase(account.getRole())) {
                    adminLoop();
                } else {
                    userLoop();
                }
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
            System.out.println("0. Wyloguj");
            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    listBooks();
                    break;
                case "2":
                    searchBooks();
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
        books.add(new Book(isbn, title, author));
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
}
