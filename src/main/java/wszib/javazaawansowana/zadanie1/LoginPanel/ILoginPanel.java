package wszib.javazaawansowana.zadanie1.LoginPanel;

import wszib.javazaawansowana.zadanie1.model.Account;

public interface ILoginPanel {
    Account authenticate(String username, String password);
}
