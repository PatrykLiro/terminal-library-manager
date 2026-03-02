package wszib.javazaawansowana.zadanie1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
}

