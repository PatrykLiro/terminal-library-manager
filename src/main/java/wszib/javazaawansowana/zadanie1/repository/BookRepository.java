package wszib.javazaawansowana.zadanie1.repository;

import wszib.javazaawansowana.zadanie1.model.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();
    List<Book> searchByTitleOrAuthor(String q);
    void save(Book book);
    boolean deleteByIsbn(String isbn);
    Book findByIsbn(String isbn);
    boolean update(Book book);
}

