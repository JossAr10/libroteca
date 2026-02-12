package com.ezertech.libroteca.service;

import com.ezertech.libroteca.exception.BookNotAvailableException;
import com.ezertech.libroteca.exception.BookNotFoundException;
import com.ezertech.libroteca.model.Book;
import com.ezertech.libroteca.model.BookStatus;
import com.ezertech.libroteca.model.Loan;
import com.ezertech.libroteca.repository.BookRepository;
import com.ezertech.libroteca.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional // Asegura que si algo falla, se reviertan los cambios en BD
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    // 1. Registrar nuevo libro
    public Book registerBook(Book book) {
        // Validar ISBN único
        Optional<Book> existing = bookRepository.findByIsbn(book.getIsbn());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Ya existe un libro con el ISBN: " + book.getIsbn());
        }

        // Establecer estado inicial y guardar
        book.setStatus(BookStatus.AVAILABLE);
        // La fecha createdAt se maneja automáticamente en la entidad con @PrePersist
        return bookRepository.save(book);
    }

    // 2. Realizar préstamo
    public Loan borrowBook(Long bookId, String borrowerName, String borrowerEmail) {
        // Buscar el libro
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        // Validar disponibilidad
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException(book.getIsbn());
        }

        // 1. Cambiar estado del libro a PRESTADO (BORROWED)
        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        // 2. Crear registro de Préstamo
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setBorrowerName(borrowerName);
        loan.setBorrowerEmail(borrowerEmail);
        loan.setLoanDate(LocalDate.now()); // Fecha de hoy
        loan.setDueDate(LocalDate.now().plusDays(14)); // Vence en 14 días

        // 3. Guardar y retornar
        return loanRepository.save(loan);
    }

    // 3. Devolver libro
    public Book returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (loan.getReturnDate() != null) {
            throw new RuntimeException("Este préstamo ya fue devuelto anteriormente.");
        }

        // 1. Establecer fecha de devolución real
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        // 2. Liberar el libro (Cambiar estado a AVAILABLE)
        Book book = loan.getBook();
        book.setStatus(BookStatus.AVAILABLE);

        return bookRepository.save(book);
    }

    // 4. Buscar libros
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.searchByTitleOrAuthor(keyword);
    }

    // 5. Obtener estadísticas para el Dashboard
    public Map<String, Object> getLibraryStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalBooks", bookRepository.count());
        stats.put("availableBooks", bookRepository.findByStatus(BookStatus.AVAILABLE).size());
        stats.put("borrowedBooks", bookRepository.findByStatus(BookStatus.BORROWED).size());
        stats.put("activeLoans", loanRepository.findByReturnDateIsNull().size());
        stats.put("overdueLoans", loanRepository.findOverdueLoans().size());

        return stats;
    }
}