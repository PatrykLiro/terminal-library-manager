package wszib.javazaawansowana.zadanie1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String username;
    private String passwordHash;
    private String role;
}
