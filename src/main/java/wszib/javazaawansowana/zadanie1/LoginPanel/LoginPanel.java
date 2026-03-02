package wszib.javazaawansowana.zadanie1.LoginPanel;

import wszib.javazaawansowana.zadanie1.model.Account;
import wszib.javazaawansowana.zadanie1.repository.UserRepository;
import wszib.javazaawansowana.zadanie1.util.PasswordHash;

public class LoginPanel implements ILoginPanel {
    private final UserRepository userRepository;

    public LoginPanel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Account authenticate(String username, String password) {
        Account account = userRepository.findByUsername(username);
        if (account != null) {
            if (PasswordHash.verify(password, account.getPasswordHash())) {
                return account;
            }
        }
        return null;
    }
}
