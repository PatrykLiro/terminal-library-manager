package wszib.javazaawansowana.zadanie1.database;

import wszib.javazaawansowana.zadanie1.model.Account;

public interface IAccounts {
    Account findByUsername(String username);
}
