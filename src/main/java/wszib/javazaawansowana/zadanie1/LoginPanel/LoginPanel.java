package wszib.javazaawansowana.zadanie1.LoginPanel;

import org.apache.commons.codec.digest.DigestUtils;
import wszib.javazaawansowana.zadanie1.database.IAccounts;
import wszib.javazaawansowana.zadanie1.model.Account;

public class LoginPanel implements ILoginPanel {
    private final IAccounts accounts;

    public LoginPanel(IAccounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public Account authenticate(String username, String password) {
        Account account = accounts.findByUsername(username);
        if (account != null) {
            String Hashpass = DigestUtils.md2Hex(password);
            if (Hashpass.equals(account.getPasswordHash())) {
                return account;
            }
        }
        return null;
    }
}
