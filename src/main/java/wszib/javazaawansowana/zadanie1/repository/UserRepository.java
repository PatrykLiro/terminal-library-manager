package wszib.javazaawansowana.zadanie1.repository;

import wszib.javazaawansowana.zadanie1.model.Account;

public interface UserRepository {
    Account findByUsername(String username);
    void save(Account account);
}

