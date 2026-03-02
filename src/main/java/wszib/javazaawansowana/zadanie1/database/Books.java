package wszib.javazaawansowana.zadanie1.database;

public class Books implements IBooks {
    @Override
    public java.util.List<wszib.javazaawansowana.zadanie1.model.Book> findAll() {
        return java.util.Collections.emptyList();
    }

    @Override
    public java.util.List<wszib.javazaawansowana.zadanie1.model.Book> searchByTitleOrAuthor(String q) {
        return java.util.Collections.emptyList();
    }

    @Override
    public void add(wszib.javazaawansowana.zadanie1.model.Book book) {
    }

    @Override
    public boolean removeByIsbn(String isbn) {
        return false;
    }

    @Override
    public wszib.javazaawansowana.zadanie1.model.Book findByIsbn(String isbn) {
        return null;
    }

    @Override
    public boolean update(wszib.javazaawansowana.zadanie1.model.Book book) {
        return false;
    }
}
