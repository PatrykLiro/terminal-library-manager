package wszib.javazaawansowana.zadanie1.repository;

import wszib.javazaawansowana.zadanie1.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();
    Category findByName(String name);
    void save(Category category);
    boolean deleteById(Long id);
}

