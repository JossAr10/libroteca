package com.ezertech.libroteca.repository;

import com.ezertech.libroteca.model.Book;
import com.ezertech.libroteca.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // 1. Buscar por ISBN exacto
    Optional<Book> findByIsbn(String isbn);

    // 2. Buscar por estado (AVAILABLE, BORROWED, etc.)
    List<Book> findByStatus(BookStatus status);

    // 3. Buscar libros por autor
    // Spring Data JPA traduce esto automáticamente a SQL con LIKE %author%
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // 4. Búsqueda compleja por título O autor (JPQL)
    // Esta consulta busca la palabra clave en ambos campos ignorando mayúsculas/minúsculas
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchByTitleOrAuthor(@Param("keyword") String keyword);
}