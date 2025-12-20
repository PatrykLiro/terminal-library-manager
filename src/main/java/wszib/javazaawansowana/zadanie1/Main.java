package wszib.javazaawansowana.zadanie1;

import wszib.javazaawansowana.zadanie1.database.Accounts;
import wszib.javazaawansowana.zadanie1.database.Books;
import wszib.javazaawansowana.zadanie1.gui.Gui;
import wszib.javazaawansowana.zadanie1.LoginPanel.LoginPanel;

public class Main {
    public static void main(String[] args) {
        Accounts accounts = new Accounts();
        Books books = new Books();
        LoginPanel loginPanel = new LoginPanel(accounts);
        Gui gui = new Gui(books, loginPanel);
        gui.start();
    }
}
