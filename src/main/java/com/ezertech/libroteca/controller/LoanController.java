package com.ezertech.libroteca.controller;

import com.ezertech.libroteca.dto.LoanRequest;
import com.ezertech.libroteca.model.Book;
import com.ezertech.libroteca.model.Loan;
import com.ezertech.libroteca.repository.LoanRepository;
import com.ezertech.libroteca.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LibraryService libraryService;
    private final LoanRepository loanRepository;

    public LoanController(LibraryService libraryService, LoanRepository loanRepository) {
        this.libraryService = libraryService;
        this.loanRepository = loanRepository;
    }

    // POST /api/loans → Crear préstamo
    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanRequest request) {
        Loan loan = libraryService.borrowBook(
                request.bookId(),
                request.borrowerName(),
                request.borrowerEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    // PUT /api/loans/{id}/return → Devolver libro
    @PutMapping("/{id}/return")
    public ResponseEntity<Book> returnBook(@PathVariable Long id) {
        Book returnedBook = libraryService.returnBook(id);
        return ResponseEntity.ok(returnedBook);
    }

    // GET /api/loans/overdue → Préstamos vencidos
    @GetMapping("/overdue")
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        return ResponseEntity.ok(loanRepository.findOverdueLoans());
    }

    // GET /api/loans/active → Préstamos activos
    @GetMapping("/active")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        return ResponseEntity.ok(loanRepository.findByReturnDateIsNull());
    }
}