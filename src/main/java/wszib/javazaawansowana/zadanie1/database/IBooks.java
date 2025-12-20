package wszib.javazaawansowana.zadanie1.database;

import java.util.List;
import wszib.javazaawansowana.zadanie1.model.Book;


public interface IBooks {
    List<Book> findAll();
    List<Book> searchByTitleOrAuthor(String q);
    void add(Book book);
    boolean removeByIsbn(String isbn);
    Book findByIsbn(String isbn);
    boolean update(Book book);
}
