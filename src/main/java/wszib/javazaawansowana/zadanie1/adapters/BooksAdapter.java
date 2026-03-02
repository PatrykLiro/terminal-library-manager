package wszib.javazaawansowana.zadanie1.adapters;

import wszib.javazaawansowana.zadanie1.database.IBooks;
import wszib.javazaawansowana.zadanie1.model.Book;
import wszib.javazaawansowana.zadanie1.repository.BookRepository;

import java.util.List;

public class BooksAdapter implements IBooks {
    private final BookRepository repo;

    public BooksAdapter(BookRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Book> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Book> searchByTitleOrAuthor(String q) {
        return repo.searchByTitleOrAuthor(q);
    }

    @Override
    public void add(Book book) {
        repo.save(book);
    }

    @Override
    public boolean removeByIsbn(String isbn) {
        return repo.deleteByIsbn(isbn);
    }

    @Override
    public Book findByIsbn(String isbn) {
        return repo.findByIsbn(isbn);
    }

    @Override
    public boolean update(Book book) {
        return repo.update(book);
    }
}

