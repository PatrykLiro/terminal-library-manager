package wszib.javazaawansowana.zadanie1.database;

import wszib.javazaawansowana.zadanie1.model.Book;
import java.util.ArrayList;
import java.util.List;

public class Books implements IBooks {
    private final List<Book> books = new ArrayList<>();

    public Books() {
        add(new Book("9780140444308", "Pan Tadeusz", "Adam Mickiewicz"));
        add(new Book("9788372320295", "Lalka", "Bolesław Prus"));
        add(new Book("9788373273894", "Zbrodnia i kara", "Fiodor Dostojewski"));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    @Override
    public List<Book> searchByTitleOrAuthor(String q) {
        String qq = q.toLowerCase();
        List<Book> res = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(qq) || b.getAuthor().toLowerCase().contains(qq)) {
                res.add(b);
            }
        }
        return res;
    }

    @Override
    public void add(Book book) {
        books.add(book);
    }

    @Override
    public boolean removeByIsbn(String isbn) {
        return books.removeIf(b -> b.getIsbn().equals(isbn));
    }

    @Override
    public Book findByIsbn(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) return b;
        }
        return null;
    }

    @Override
    public boolean update(Book book) {
        Book existing = findByIsbn(book.getIsbn());
        if (existing != null) {
            existing.setTitle(book.getTitle());
            existing.setAuthor(book.getAuthor());
            return true;
        }
        return false;
    }
}
