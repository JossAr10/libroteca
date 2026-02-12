package com.ezertech.libroteca.controller;

import com.ezertech.libroteca.exception.BookNotFoundException;
import com.ezertech.libroteca.model.Book;
import com.ezertech.libroteca.model.BookStatus;
import com.ezertech.libroteca.repository.BookRepository;
import com.ezertech.libroteca.service.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final LibraryService libraryService;
    private final BookRepository bookRepository;

    // POST /api/books → Crear libro
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book created = libraryService.registerBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/books → Listar todos
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    // GET /api/books/{id} → Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return ResponseEntity.ok(book);
    }

    // GET /api/books/search?q=keyword → Buscar
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(name = "q") String keyword) {
        return ResponseEntity.ok(libraryService.searchBooks(keyword));
    }

    // DELETE /api/books/{id} → Eliminar (solo si disponible)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            // Podríamos crear una excepción específica, pero un Bad Request basta por ahora
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        bookRepository.delete(book);
        return ResponseEntity.noContent().build();
    }
}