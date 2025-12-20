package wszib.javazaawansowana.zadanie1.database;

import wszib.javazaawansowana.zadanie1.model.Account;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;

public class Accounts implements IAccounts {
    private final List<Account> accounts = new ArrayList<>();

    public Accounts() {
        this.accounts.add(new Account("admin", DigestUtils.md2Hex("admin"), "ADMIN"));
        this.accounts.add(new Account("user", DigestUtils.md2Hex("user"), "USER"));
    }

    @Override
    public Account findByUsername(String username) {
        for (Account a : accounts) {
            if (a.getUsername().equals(username)) return a;
        }
        return null;
    }
}
